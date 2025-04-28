#define UNIX_TO_POSTGRES  -946695600000l
#define _POSIX_C_SOURCE    200809l
#define PGMCISIDV1_SUCCESS 0
#define PGMCISIDV1_RESET   1
#define PGMCISIDV1_ERROR   2

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

#include "mcisid/mcisidv1.h"
#include "mcisid/mcisid.h"

PG_MODULE_MAGIC;

extern void _PG_init(void);
extern void _PG_fini(void);
static int epoch;

mcisidstate pgmcisidv1GetSequence(Relation rel, int8_t model, mcisidv1seq* sequence) {
  TupleDesc tupdesc = RelationGetDescr(rel);
  Snapshot snapshot = GetTransactionSnapshot();
  TableScanDesc scan;
  HeapTuple tuple;
  TM_Result result;
  mcisidstate state;
  bool isnull;
  scan = table_beginscan(rel, snapshot, 0, NULL);
  while (true) {
    tuple = heap_getnext(scan, ForwardScanDirection);
    if (tuple == NULL) {
      mcisidv1ResetSequence(sequence);
      state = PGMCISIDV1_RESET;
      break;
    }
    if (DatumGetInt16(heap_getattr(tuple, 1, tupdesc, &isnull)) != model)
      continue;
    sequence->current = DatumGetInt32(heap_getattr(tuple, 2, tupdesc, &isnull));
    sequence->lastime = (time_t)DatumGetInt64(heap_getattr(tuple, 3, tupdesc, &isnull));
    CommandId cmdid = GetCurrentCommandId(true);
    result = heap_delete(rel, &tuple->t_self, cmdid, snapshot, true, NULL, false);
    if (result != TM_Ok)
      state = PGMCISIDV1_ERROR;
    state = PGMCISIDV1_SUCCESS;
    break;
  }
  table_endscan(scan);
  return state;
}

void pgmcisidv1ReflectInInsertSnapshot(const mcisidv1snapshot* snapshot, Relation rel) {
  TupleDesc tupdesc = RelationGetDescr(rel);
  Datum values[3];
  bool nulls[3] = {false, false, false};
  values[0] = Int16GetDatum(snapshot->model);
  values[1] = Int32GetDatum(snapshot->seqnext);
  values[2] = Int64GetDatum(snapshot->instant);
  HeapTuple newtuple = heap_form_tuple(tupdesc, values, nulls);
  TupleTableSlot* slot = MakeSingleTupleTableSlot(tupdesc, &TTSOpsHeapTuple);
  ExecStoreHeapTuple(newtuple, slot, false);
  simple_table_tuple_insert(rel, slot);
  ExecDropSingleTupleTableSlot(slot);
}

time_t getInstantInMilis() {
  struct timespec ts;
  clock_gettime(CLOCK_REALTIME, &ts);
  return (time_t)((ts.tv_sec * 1000) + (ts.tv_nsec / 1000000L));
}

void _PG_init(void) {
  mcisidInit();
  DefineCustomIntVariable(
    "mcisidv1.epoch",
    "Epoch variable, defined as the number of seconds since Unix epoch.",
    NULL,
    &epoch,
    0,
    0,
    __INT32_MAX__,
    PGC_SUSET,
    0,
    NULL,
    NULL,
    NULL
  );
}

PG_FUNCTION_INFO_V1(pgmcisidTypeInput);
Datum pgmcisidTypeInput(PG_FUNCTION_ARGS) {
  const char* internal = PG_GETARG_CSTRING(0);
  char* pgid = (char*)palloc(sizeof(mcisidv1));
  for (uint8_t i = 0; i < MCISIDV1_IOSIZE; ++i) {
    if (i < MCISIDV1_IOSIZE && internal[i] == '\0') {
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
  if (internal[MCISIDV1_IOSIZE] != '\0') {
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

PG_FUNCTION_INFO_V1(pgmcisidv1TextToMcisidv1);
Datum pgmcisidv1TextToMcisidv1(PG_FUNCTION_ARGS) {
  text* ptr = PG_GETARG_TEXT_P(0);
  char* content = VARDATA_ANY(ptr);
  uint64_t length = VARSIZE(ptr) - VARHDRSZ;
  if (length != MCISIDV1_IOSIZE)
    ereport(ERROR,
            (errmsg("O ID deve conter exatamente 12 caracteres.")));
  char* pgid = (char*)palloc(sizeof(mcisidv1));
  for (uint8_t i = 0; i < MCISIDV1_IOSIZE; ++i) {
    if (mcisidGetLookup(content[i]) == MCISID_INVALID_CHARACTER) {
      pfree(pgid);
      ereport(ERROR,
            (errmsg("O caractere: %c é inválido para o ID.", content[i])));
    }
    pgid[i] = content[i];
  }
  PG_RETURN_POINTER((mcisidv1*)pgid);
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

PG_FUNCTION_INFO_V1(pgmcisidv1Generate);
Datum pgmcisidv1Generate(PG_FUNCTION_ARGS) {
  int16 model = PG_GETARG_INT16(0);
  if (model < 0 || model > 0x3F)
    ereport(ERROR,
            (errmsg("Model repr is 6bits and positive.")));
  LOCKTAG tag;
  SET_LOCKTAG_ADVISORY(tag,
    (uint32) MyDatabaseId,
    (uint32) 0,
    (uint32) 0,
    (uint32) model);
  if (LockAcquire(&tag, ExclusiveLock, true, false) != LOCKACQUIRE_OK)
    ereport(ERROR, (errmsg("Failed to acquire lock for model %d. Another transaction might be using it.", model)));
  time_t instant = getInstantInMilis() - ((time_t)epoch * 1000);
  mcisidstate status = MCISID_NO_RESPONSE;
  mcisidv1seq sequence;
  mcisidv1snapshot snapshot;
  Oid relid = PG_GETARG_OID(1);
  Relation rel;
  MemoryContext oldctx;
  ResourceOwner oldowner;
  if (!OidIsValid(relid)) 
    ereport(ERROR,
            (errmsg("Table not found.")));
  rel = table_open(relid, RowExclusiveLock);
  oldctx =  CurrentMemoryContext;
  oldowner = CurrentResourceOwner;
  PG_TRY(); {
    status = pgmcisidv1GetSequence(rel, model, &sequence);
    if (status == PGMCISIDV1_RESET)
      sequence.lastime = instant;
    if (status != PGMCISIDV1_SUCCESS && status != PGMCISIDV1_RESET)
      ereport(ERROR,
              (errmsg("Um erro ao ler uma sequência ocorreu. Status: %i", status)));
    status = MCISID_NO_RESPONSE;
    status = mcisidv1NextValue(&sequence, instant);
    for (uint8_t i = 0; i < 10; ++i) {
      if (status == MCISIDV1_SUCCESS)
        break;
      else if (status == MCISIDV1_OVERFLOW)
        pg_usleep(1000);
      else if (status == MCISIDV1_RESET)
        mcisidv1ResetSequence(&sequence);
      else if (status == MCISIDV1_INSTANT_CORRUPTED)
        instant = getInstantInMilis() - ((time_t)epoch * 1000);
      else break;
      status = mcisidv1NextValue(&sequence, instant);
    }
    if (status != MCISIDV1_SUCCESS)
      ereport(ERROR,
              (errmsg("Um erro no deslocamento da sequência ocorreu: %i (macro).", status)));
    snapshot = mcisidv1TakeSnapshot(&sequence, model);
    pgmcisidv1ReflectInInsertSnapshot(&snapshot, rel);
  }
  PG_CATCH(); {
    table_close(rel, RowExclusiveLock);
    LockRelease(&tag, ExclusiveLock, true);
    MemoryContextSwitchTo(oldctx);
    CurrentResourceOwner = oldowner;
    PG_RE_THROW();
  }
  PG_END_TRY();
  table_close(rel, RowExclusiveLock);
  LockRelease(&tag, ExclusiveLock, true);
  mcisidv1* id = (mcisidv1*)palloc(sizeof(mcisidv1));
  mcisidv1WriteOutput(&snapshot, id);
  PG_RETURN_POINTER(id);
}

PG_FUNCTION_INFO_V1(pgmcisidv1GetInstant);
Datum pgmcisidv1GetInstant(PG_FUNCTION_ARGS) {
  mcisidv1* pgid = (mcisidv1*)PG_GETARG_POINTER(0);
  PG_RETURN_INT64((int64)mcisidv1GetTimeInstantMilis(pgid));
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
  time_t epochInMilis = ((time_t)epoch * 1000l) + (time_t)UNIX_TO_POSTGRES;
  PG_RETURN_TIMESTAMP((Timestamp)epochInMilis * 1000l);
}

PG_FUNCTION_INFO_V1(pgmcisidv1CreatedAt);
Datum pgmcisidv1CreatedAt(PG_FUNCTION_ARGS) {
  mcisidv1* pgid = (mcisidv1*)PG_GETARG_POINTER(0);
  time_t instant = mcisidv1GetTimeInstantMilis(pgid);
  time_t createdAtInMilis = ((time_t)epoch * 1000 + instant) + (time_t)UNIX_TO_POSTGRES;
  PG_RETURN_TIMESTAMP((Timestamp)(createdAtInMilis * 1000));
}