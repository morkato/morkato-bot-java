#define UNIX_TO_POSTGRES -946684800000
#define _POSIX_C_SOURCE 200809l

#include "postgres.h"
#include "catalog/pg_sequence.h"
#include "executor/executor.h"
#include "storage/lockdefs.h"
#include "storage/lock.h"
#include "storage/proc.h"
#include "storage/lmgr.h"
#include "access/htup_details.h"
#include "access/relation.h"
#include "access/tableam.h"
#include "access/heapam.h"
#include "access/table.h"
#include "access/htup.h"
#include "commands/sequence.h"
#include "utils/relcache.h"
#include "utils/timestamp.h"
#include "utils/lsyscache.h"
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
static bool ready;
static int _epoch;

static void pgsetmcisidv1Epoch(const int newval, void *extra) {
  generator.epoch = newval;
}

void _PG_init(void) {
  mcisidInit();
  mcisidv1BootGenerator(&generator, sequences);
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
  elog(NOTICE, "Carregado");
  pg_usleep(1000);
  ready = true;
}

PG_FUNCTION_INFO_V1(pgmcisidTypeInput);
Datum pgmcisidTypeInput(PG_FUNCTION_ARGS) {
  const char* internal = PG_GETARG_CSTRING(0);
  char* pgid = (char*)palloc(sizeof(mcisidv1));
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
    pgid[i] = internal[i];
  }
  if (internal[MCISIDV1_SIZE] != '\0') {
    pfree(pgid);
    ereport(ERROR,
          (errmsg("O ID deve conter exatamente 12 caracteres.")));
  }
  PG_RETURN_POINTER((mcisidv1*)pgid);
}

PG_FUNCTION_INFO_V1(pgmcisidTypeOutput);
Datum pgmcisidTypeOutput(PG_FUNCTION_ARGS) {
  mcisidv1* pgid = (mcisidv1*)PG_GETARG_POINTER(0);
  char* id = (char*)palloc(sizeof(mcisidv1) + 1);
  memcpy(id, pgid, sizeof(mcisidv1));
  id[sizeof(mcisidv1)] = '\0';
  PG_RETURN_CSTRING(id);
}

PG_FUNCTION_INFO_V1(pgmcisidv1cmp);
Datum pgmcisidv1cmp(PG_FUNCTION_ARGS) {
  mcisidv1* a = (mcisidv1*)PG_GETARG_POINTER(0);
  mcisidv1* b = (mcisidv1*)PG_GETARG_POINTER(1);
  mcisidv1cmp cmp = mcisidv1Compare(a, b);
  if (cmp == MCISIDV1_COMPARE_GREATER)
    PG_RETURN_INT32(1);
  else if (cmp == MCISIDV1_COMPARE_EQUAL)
    PG_RETURN_INT32(0);
  else if (cmp == MCISIDV1_COMPARE_LESS)
    PG_RETURN_INT32(-1);
  ereport(ERROR,
          (errmsg("Um estado não esperado ocorreu. Estado: %i verifique.", cmp)));
}

PG_FUNCTION_INFO_V1(pgmcisidv1eq);
Datum pgmcisidv1eq(PG_FUNCTION_ARGS) {
  mcisidv1* a = (mcisidv1*)PG_GETARG_POINTER(0);
  mcisidv1* b = (mcisidv1*)PG_GETARG_POINTER(1);
  PG_RETURN_BOOL(mcisidv1Compare(a, b) == MCISIDV1_COMPARE_EQUAL);
}

PG_FUNCTION_INFO_V1(pgmcisidv1lt);
Datum pgmcisidv1lt(PG_FUNCTION_ARGS) {
  mcisidv1* a = (mcisidv1*)PG_GETARG_POINTER(0);
  mcisidv1* b = (mcisidv1*)PG_GETARG_POINTER(1);
  PG_RETURN_BOOL(mcisidv1Compare(a, b) == MCISIDV1_COMPARE_LESS);
}

PG_FUNCTION_INFO_V1(pgmcisidv1gt);
Datum pgmcisidv1gt(PG_FUNCTION_ARGS) {
  mcisidv1* a = (mcisidv1*)PG_GETARG_POINTER(0);
  mcisidv1* b = (mcisidv1*)PG_GETARG_POINTER(1);
  PG_RETURN_BOOL(mcisidv1Compare(a, b) == MCISIDV1_COMPARE_GREATER);
}

PG_FUNCTION_INFO_V1(pgmcisidv1NextVal);
Datum pgmcisidv1NextVal(PG_FUNCTION_ARGS) {
  int16 model = PG_GETARG_INT16(0) & 0x3F;
  Oid relid;
  Relation rel;
  TableScanDesc scan;
  HeapTuple tuple;
  TupleDesc tupdesc;
  bool isnull;
  Datum datum;
  mcisidv1seq sequence;

  relid = PG_GETARG_OID(1);
  if (!OidIsValid(relid))
    ereport(ERROR,
            (errmsg("Table not found.")));
  rel = table_open(relid, AccessShareLock);
  tupdesc = RelationGetDescr(rel);
  scan = table_beginscan(rel, GetActiveSnapshot(), 0, NULL);
  while ((tuple = heap_getnext(scan, ForwardScanDirection)) != NULL) {
    datum = heap_getattr(tuple, 1, tupdesc, &isnull);
    if (isnull && DatumGetInt16(datum) != model)
      continue;
    sequence.current = (uint32_t)DatumGetInt32(heap_getattr(tuple, 2, tupdesc, &isnull));
    sequence.lastime = (time_t)DatumGetInt64(heap_getattr(tuple, 3, tupdesc, &isnull));
    time_t instant = getInstantInMilis() - _epoch;
    ++sequence.current;
    sequence.lastime = instant;
  }
  table_endscan(scan);
  table_close(rel, AccessShareLock);
}

PG_FUNCTION_INFO_V1(pgmcisidv1Generate);
Datum pgmcisidv1Generate(PG_FUNCTION_ARGS) {
  if (generator.epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    ereport(ERROR,
            (errmsg("Epoch is not initialized. Set 'mcisidv1.epoch' before calling this function.")));
  if (!ready)
    ereport(ERROR,
            (errmsg("Extension not prepared for generate ids.")));
  int16 model = PG_GETARG_INT16(0);
  int status = MCISID_NO_RESPONSE;
  mcisidv1 mcid;
  if (model < 0 || model > MCISIDV1_IDENTIFER_MASK) 
    ereport(ERROR,
            (errmsg("Model repr is 6bits and positive.")));
  LOCKTAG tag;
  SET_LOCKTAG_ADVISORY(tag,
    (uint32) MyDatabaseId,
    (uint32) 0,
    (uint32) 0,
    (uint32) model);
  LockAcquireResult result = LockAcquire(&tag, ExclusiveLock, true, false);
  if (result != LOCKACQUIRE_OK)
    ereport(ERROR, (errmsg("Failed to acquire lock for model %d. Another transaction might be using it.", model)));
  PG_TRY(); {
    
  }
  PG_FINALLY(); {
    LockRelease(&tag, ExclusiveLock, true);
  }
  PG_END_TRY();
  status = mcisidv1Generate(&mcid, &generator, model);
  for (uint8_t tries = 0; tries < 10; ++tries) {
    if (status == MCISIDV1_SUCCESS) {
      LockRelease(&tag, ExclusiveLock, true);
      mcisidv1* pgid = (mcisidv1*)palloc(sizeof(mcisidv1));
      memcpy(pgid, &mcid, sizeof(mcisidv1));
      PG_RETURN_POINTER(pgid);
    } else if (status == MCISIDV1_RESET) {
      mcisidv1ResetSequence(&generator, model);
      status = mcisidv1Generate(&mcid, &generator, model);
      continue;
    } else if (status == MCISIDV1_OVERFLOW) {
      pg_usleep(200);
      status = mcisidv1Generate(&mcid, &generator, model);
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

PG_FUNCTION_INFO_V1(pgmcisidv1GetInstant);
Datum pgmcisidv1GetInstant(PG_FUNCTION_ARGS) {
  mcisidv1* pgid = (mcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT64((int64)mcisidv1GetTimeMilis(pgid));
}

PG_FUNCTION_INFO_V1(pgmcisidv1CreatedAt);
Datum pgmcisidv1CreatedAt(PG_FUNCTION_ARGS) {
  if (generator.epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    ereport(ERROR,
            (errmsg("Epoch is not initialized")));
  mcisidv1* pgid = (mcisidv1*)PG_GETARG_POINTER(0);
  time_t createdAt = mcisidv1CreatedAt(&generator, pgid);
  Timestamp timestamp = (Timestamp)(createdAt + UNIX_TO_POSTGRES) * (Timestamp)1000;
  PG_RETURN_TIMESTAMP(timestamp);
}

PG_FUNCTION_INFO_V1(pgmcisidv1OriginModel);
Datum pgmcisidv1OriginModel(PG_FUNCTION_ARGS) {
  mcisidv1* pgid = (mcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT16((int16)mcisidv1GetOriginModel(pgid));
}

PG_FUNCTION_INFO_V1(pgmcisidv1InstantSequence);
Datum pgmcisidv1InstantSequence(PG_FUNCTION_ARGS) {
  mcisidv1* pgid = (mcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT64((int64)mcisidv1GetSequence(pgid));
}

PG_FUNCTION_INFO_V1(pgmcisidv1GetEpoch);
Datum pgmcisidv1GetEpoch(PG_FUNCTION_ARGS) {
  if (generator.epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    ereport(ERROR,
            (errmsg("Epoch is not initialized")));
  Timestamp milis = ((Timestamp)generator.epoch * 1000) + ((Timestamp)UNIX_TO_POSTGRES);
  Timestamp timestamp = milis * 1000;
  PG_RETURN_TIMESTAMP(timestamp);
}