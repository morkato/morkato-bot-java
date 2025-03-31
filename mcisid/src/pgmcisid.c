#include <postgresql/17/server/postgres.h>
#include <postgresql/17/server/access/relation.h>
#include <postgresql/17/server/access/tableam.h>
#include <postgresql/17/server/access/htup.h>
#include <postgresql/17/server/utils/rel.h>
#include <postgresql/17/server/utils/relcache.h>
#include <postgresql/17/server/executor/executor.h>
#include <postgresql/17/server/storage/lockdefs.h>
#include <postgresql/17/server/storage/lock.h>
#include <postgresql/17/server/storage/proc.h>
#include <postgresql/17/server/storage/lmgr.h>
#include <postgresql/17/server/funcapi.h>
#include <postgresql/17/server/fmgr.h>
#include <postgresql/17/server/catalog/pg_sequence.h>
#include <postgresql/17/server/commands/sequence.h>
#include <postgresql/17/server/utils/builtins.h>
#include <postgresql/17/server/utils/guc.h>
#include <postgresql/17/server/port.h>
#include <postgresql/17/server/miscadmin.h>
#include "mcisid.h"
PG_MODULE_MAGIC;

extern void _PG_init(void);
extern void _PG_fini(void);

static mcisidv1gen gens[64];
static uint64_t initializationTimeSeconds;
static int epochInDays;

typedef struct __attribute__((packed)) {
  char data[MCISIDV1_SIZE];
} pgmcisidv1;

void _PG_init(void) {
  DefineCustomIntVariable(
    "mcisidv1.epoch",
    "Epoch variable defined quantity of days to initilaze.",
    NULL,
    &epochInDays,
    -1,
    0,
    0xFFFF,
    PGC_SUSET,
    0,
    NULL,
    NULL,
    NULL
  );
  mcisidInit();
  mcisidv1SetGenerators(gens);
  mcisidv1SetSafeGeneration(true);
  initializationTimeSeconds = time(NULL);
  for (uint8_t i = 0; i < 64; ++i) {
    mcisidCreate(gens + i);
  }
}

PG_FUNCTION_INFO_V1(mcisidv1pGenerate);
Datum mcisidv1pGenerate(PG_FUNCTION_ARGS) {
  if (epochInDays == -1)
    ereport(ERROR,
            (errmsg("Epoch is not initialized")));
  mcisidv1SetEpoch(epochInDays * 24 * 60 * 60);
  int16 model = PG_GETARG_INT16(0);
  char id[MCISIDV1_SIZE];
  if (model < 0 || model > 0b111111) 
    ereport(ERROR,
            (errmsg("Model repr is 6bits and positive.")));
  if (initializationTimeSeconds == time(NULL))
    pg_usleep(1000000);
  LOCKTAG tag;
  SET_LOCKTAG_ADVISORY(tag,
    (uint32) MyDatabaseId,
    (uint32) 0,
    (uint32) 0,
    (uint32) model);
  LockAcquireResult result = LockAcquire(&tag, ExclusiveLock, true, false);
  if (result != LOCKACQUIRE_OK)
    ereport(ERROR, (errmsg("Failed to acquire lock for model %d", model)));
  int status = MCISID_NO_RESPONSE;
  status = mcisidv1Generate(id, model);
  for (uint8_t tries = 0; tries < 10; ++tries) {
    if (status == MCISIDV1_SUCCESS) {
      LockRelease(&tag, ExclusiveLock, true);
      pgmcisidv1* pgid = (pgmcisidv1*)palloc(sizeof(pgmcisidv1));
      memcpy(pgid->data, id, MCISIDV1_SIZE);
      PG_RETURN_POINTER(pgid);
    } else if (status == MCISIDV1_RESET) {
      mcisidv1ResetSequence(model);
      status = mcisidv1Generate(id, model);
      continue;
    } else if (status == MCISIDV1_OVERFLOW) {
      pg_usleep(100000);
      status = mcisidv1Generate(id, model);
      continue;
    }
    LockRelease(&tag, ExclusiveLock, true);
    ereport(ERROR,
            (errmsg("An unexpected error occurred. Status: %i", status)));
  }
  LockRelease(&tag, ExclusiveLock, true);
  ereport(ERROR,
      (errmsg("SEQUENCE id is overflowed after 10° tries. Try again later.")));
}

PG_FUNCTION_INFO_V1(mcisidTypeInput);
Datum mcisidTypeInput(PG_FUNCTION_ARGS) {
  const char* internal = PG_GETARG_CSTRING(0);
  pgmcisidv1* pgid = (pgmcisidv1*)palloc(sizeof(pgmcisidv1));
  for (uint8_t i = 0; i < MCISIDV1_SIZE; ++i) {
    if (i < MCISIDV1_SIZE && internal[i] == '\0') {
      pfree(pgid);
      ereport(ERROR,
            (errmsg("O ID deve conter exatamente 12 caracteres.")));
    } else if (mcisidGetLookup(internal[i]) == MCISID_INVALID_CHARACTER) {
      pfree(pgid);
      ereport(ERROR,
            (errmsg("O caractere: %c é inválido para o ID.", internal[i])));
    }
    pgid->data[i] = internal[i];
  }
  if (internal[MCISIDV1_SIZE] != '\0') {
    pfree(pgid);
    ereport(ERROR,
          (errmsg("O ID deve conter exatamente 12 caracteres.")));
  }
  PG_RETURN_POINTER(pgid);
}

PG_FUNCTION_INFO_V1(mcisidTypeOutput);
Datum mcisidTypeOutput(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  char* id = (char*)palloc(MCISIDV1_SIZE + 1);
  memcpy(id, pgid->data, MCISIDV1_SIZE);
  id[MCISIDV1_SIZE] = '\0';
  PG_RETURN_CSTRING(id);
}

PG_FUNCTION_INFO_V1(mcisidv1GetInstant);
Datum mcisidv1GetInstant(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT64((int64)mcisidv1GetTimeSeconds(pgid->data));
}

PG_FUNCTION_INFO_V1(mcisidv1CreatedAt);
Datum mcisidv1CreatedAt(PG_FUNCTION_ARGS) {
  if (epochInDays == -1)
    ereport(ERROR,
            (errmsg("Epoch is not initialized")));
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  Timestamp timestamp = (epochInDays * 24 * 60 * 60 + mcisidv1GetTimeSeconds(pgid->data)) * 1000000;
  PG_RETURN_TIMESTAMP(timestamp);
}

PG_FUNCTION_INFO_V1(mcisidv1OriginModel);
Datum mcisidv1OriginModel(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT16((int16)mcisidv1GetOriginModel(pgid->data));
}

PG_FUNCTION_INFO_V1(mcisidv1InstantSequence);
Datum mcisidv1InstantSequence(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT64((int64)mcisidv1GetSequence(pgid->data));
}

PG_FUNCTION_INFO_V1(mcisidv1GetEpoch);
Datum mcisidv1GetEpoch(PG_FUNCTION_ARGS) {
  if (epochInDays == -1)
    ereport(ERROR,
            (errmsg("Epoch is not initialized")));
  PG_RETURN_TIMESTAMP(epochInDays * 24 * 60 * 60 * 1000000);
}