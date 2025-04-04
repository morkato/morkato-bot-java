#define _POSIX_C_SOURCE 200809l

#include "mcisid-postgres.h"
#include "storage/lock.h"
#include "access/heapam.h"
#include "access/table.h"
#include "miscadmin.h"
#include "mcisid.h"
#include <sys/time.h>

time_t getInstantInMilis() {
  struct timespec ts;
  clock_gettime(CLOCK_REALTIME, &ts);
  return (time_t)((ts.tv_sec * 1000) + (ts.tv_nsec / 1000000L));
}

int pgsaltToSeq(
  HeapTuple* tuple,
  TupleDesc* tupdesc,
  TableScanDesc* scan,
  uint8_t model
) {
  bool isnull;
  Datum datum;
  while ((*tuple = heap_getnext(*scan, ForwardScanDirection)) != NULL) {
    datum = heap_getattr(*tuple, 1, *tupdesc, &isnull);
    if (isnull || ((uint8_t)(DatumGetInt16(datum) & 0xFF)) != model)
      continue;
    return 0;
  }
  return 1;
}

int pgcreateSequenceWithDefaults(
  Relation* rel,
  HeapTuple* tuple,
  TupleDesc* tupdesc,
  TableScanDesc* scan
) {}

int mcisidv1PgOpenSequenceRelation(
  Oid relid,
  LOCKTAG* tag,
  Relation* rel,
  TableScanDesc* scan,
  uint8_t model
) {
  SET_LOCKTAG_ADVISORY(*tag,
    (uint32) MyDatabaseId,
    (uint32) 0,
    (uint32) 0,
    (uint32) model);
  LockAcquireResult result = LockAcquire(tag, ExclusiveLock, true, false);
  if (result != LOCKACQUIRE_OK)
    return 1;
  *rel = table_open(relid, RowExclusiveLock);
  *scan = table_beginscan(rel, GetActiveSnapshot(), 0, NULL);
  return 0;
}

void mcisidv1PgCloseSequenceRelation(
  LOCKTAG* tag,
  Relation* rel,
  TableScanDesc* scan
) {
  table_endscan(*scan);
  table_close(*rel, AccessShareLock);
  LockRelease(tag, ExclusiveLock, true);
}

int pgnextCycle(
  Relation* rel,
  TableScanDesc* scan,
  uint8_t model,
  uint32_t* seqnext,
  time_t* instant
) {
  HeapTuple tuple;
  TupleDesc tupdesc;
  uint32_t current;
  time_t lastime;
  int status;
  bool isnull;

  tupdesc = RelationGetDescr(*rel);
  status = pgsaltToSeq(&tuple, &tupdesc, &scan, model);
  if (status != 0)
    return 255;
  current = (uint32_t)DatumGetInt32(heap_getattr(tuple, 2, tupdesc, &isnull));
  lastime = (time_t)DatumGetInt64(heap_getattr(tuple, 3, tupdesc, &isnull));
  
}

int mcisidv1PgFindSequence(
  Oid relid,
  uint8_t model,
  uint32_t* current,
  time_t* lastime
) {
  Oid relid;
  Relation rel;
  TableScanDesc scan;
  HeapTuple tuple;
  TupleDesc tupdesc;
  bool isnull;
  int status;

  LOCKTAG tag;
  SET_LOCKTAG_ADVISORY(tag,
    (uint32) MyDatabaseId,
    (uint32) 0,
    (uint32) 0,
    (uint32) model);
  LockAcquireResult result = LockAcquire(&tag, ExclusiveLock, true, false);
  if (result != LOCKACQUIRE_OK)
    return 1;
  rel = table_open(relid, AccessShareLock);
  tupdesc = RelationGetDescr(rel);
  scan = table_beginscan(rel, GetActiveSnapshot(), 0, NULL);
  status = pgsaltToSeq(&tuple, &tupdesc, &scan, model);
  if (status != 0)
    return 2;
  *current = (uint32_t)DatumGetInt32(heap_getattr(tuple, 2, tupdesc, &isnull));
  *lastime = (time_t)DatumGetInt64(heap_getattr(tuple, 3, tupdesc, &isnull));
  table_endscan(scan);
  table_close(rel, AccessShareLock);
  LockRelease(&tag, ExclusiveLock, true);
}