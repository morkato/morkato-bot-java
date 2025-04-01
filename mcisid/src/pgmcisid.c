#define UNIX_TO_POSTGRES -946684800

#include "postgres.h"
#include "catalog/pg_sequence.h"
#include "executor/executor.h"
#include "storage/lockdefs.h"
#include "storage/lock.h"
#include "storage/proc.h"
#include "storage/lmgr.h"
#include "access/relation.h"
#include "access/tableam.h"
#include "access/htup.h"
#include "commands/sequence.h"
#include "utils/relcache.h"
#include "utils/timestamp.h"
#include "utils/builtins.h"
#include "utils/rel.h"
#include "utils/guc.h"
#include "miscadmin.h"
#include "funcapi.h"
#include "fmgr.h"
#include "port.h"

#include "mcisid.h"

PG_MODULE_MAGIC;

extern void _PG_init(void);
extern void _PG_fini(void);

static mcisidv1seq sequences[MCISIDV1_MAX_SEQUENCES];
static mcisidv1gen generator;
static time_t initializationTimeSeconds;
static int _epoch;

typedef struct __attribute__((packed)) {
  char data[MCISIDV1_SIZE];
} pgmcisidv1;

static void pgsetmcisidv1Epoch(const int newval, void *extra) {
  generator.epoch = newval;
}

void _PG_init(void) {
  mcisidInit();
  mcisidv1BootGenerator(&generator, sequences);
  initializationTimeSeconds = time(NULL);
  DefineCustomIntVariable(
    "mcisidv1.epoch",
    "Epoch variable, defined as the number of seconds since Unix epoch.",
    NULL,
    &_epoch,
    MCISIDV1_EPOCH_INVALID_VALUE,
    0,
    __INT32_MAX__,
    PGC_SUSET,
    0,
    NULL,
    pgsetmcisidv1Epoch,
    NULL
  );
}

PG_FUNCTION_INFO_V1(pgmcisidv1Generate);
Datum pgmcisidv1Generate(PG_FUNCTION_ARGS) {
  if (generator.epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    ereport(ERROR,
            (errmsg("Epoch is not initialized. Set 'mcisidv1.epoch' before calling this function.")));
  int16 model = PG_GETARG_INT16(0);
  char id[MCISIDV1_SIZE];
  if (model < 0 || model > MCISIDV1_IDENTIFER_MASK) 
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
    ereport(ERROR, (errmsg("Failed to acquire lock for model %d. Another transaction might be using it.", model)));
  int status = MCISID_NO_RESPONSE;
  status = mcisidv1Generate(id, &generator, model);
  for (uint8_t tries = 0; tries < 10; ++tries) {
    if (status == MCISIDV1_SUCCESS) {
      LockRelease(&tag, ExclusiveLock, true);
      pgmcisidv1* pgid = (pgmcisidv1*)palloc(sizeof(pgmcisidv1));
      memcpy(pgid->data, id, MCISIDV1_SIZE);
      PG_RETURN_POINTER(pgid);
    } else if (status == MCISIDV1_RESET) {
      mcisidv1ResetSequence(&generator, model);
      status = mcisidv1Generate(id, &generator, model);
      continue;
    } else if (status == MCISIDV1_OVERFLOW) {
      pg_usleep(100000);
      status = mcisidv1Generate(id, &generator, model);
      continue;
    }
    LockRelease(&tag, ExclusiveLock, true);
    ereport(ERROR,
            (errmsg("Unexpected mcisidv1 generation error. Status: %i. Check generator state.", status)));
  }
  LockRelease(&tag, ExclusiveLock, true);
  ereport(ERROR,
      (errmsg("SEQUENCE id is overflowed after 10° tries. Try again later.")));
}

PG_FUNCTION_INFO_V1(pgmcisidTypeInput);
Datum pgmcisidTypeInput(PG_FUNCTION_ARGS) {
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

PG_FUNCTION_INFO_V1(pgmcisidTypeOutput);
Datum pgmcisidTypeOutput(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  char* id = (char*)palloc(MCISIDV1_SIZE + 1);
  memcpy(id, pgid->data, MCISIDV1_SIZE);
  id[MCISIDV1_SIZE] = '\0';
  PG_RETURN_CSTRING(id);
}

PG_FUNCTION_INFO_V1(pgmcisidv1GetInstant);
Datum pgmcisidv1GetInstant(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT64((int64)mcisidv1GetTimeSeconds(pgid->data));
}

PG_FUNCTION_INFO_V1(pgmcisidv1CreatedAt);
Datum pgmcisidv1CreatedAt(PG_FUNCTION_ARGS) {
  if (generator.epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    ereport(ERROR,
            (errmsg("Epoch is not initialized")));
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  time_t createdAt = mcisidv1CreatedAt(&generator, pgid->data);
  Timestamp timestamp = (Timestamp)(createdAt + UNIX_TO_POSTGRES) * (Timestamp)1000000;
  PG_RETURN_TIMESTAMP(timestamp);
}

PG_FUNCTION_INFO_V1(pgmcisidv1OriginModel);
Datum pgmcisidv1OriginModel(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT16((int16)mcisidv1GetOriginModel(pgid->data));
}

PG_FUNCTION_INFO_V1(pgmcisidv1InstantSequence);
Datum pgmcisidv1InstantSequence(PG_FUNCTION_ARGS) {
  pgmcisidv1* pgid = (pgmcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT64((int64)mcisidv1GetSequence(pgid->data));
}

PG_FUNCTION_INFO_V1(pgmcisidv1GetEpoch);
Datum pgmcisidv1GetEpoch(PG_FUNCTION_ARGS) {
  if (generator.epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    ereport(ERROR,
            (errmsg("Epoch is not initialized")));
  Timestamp seconds = ((Timestamp)generator.epoch) + ((Timestamp)UNIX_TO_POSTGRES);
  Timestamp timestamp = seconds * 1000000;
  PG_RETURN_TIMESTAMP(timestamp);
}

