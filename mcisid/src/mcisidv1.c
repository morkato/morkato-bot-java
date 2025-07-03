#include <time.h>
#include "mcisidv1.h"
#include "mcisid.h"

/*
 * Locomove a sequência passada para frente com base no instante de tempo.
*/
mcisidstate mcisidv1NextValue(mcisidv1seq* sequence, time_t instant) {
  if (instant < sequence->lastime)
    return MCISIDV1_INSTANT_CORRUPTED;
  int32_t seqnext = sequence->current + 1;
  if (seqnext > MCISIDV1_MAX_SEQUENCE)
    /*
     * Parabéns! você quebrou as leis da física.
     * Você alcançou uma linha que nunca deveria ser atingida.
     * Você deve ter esquecido de resetar a sequência, tô certo?
     */
    return MCISIDV1_STUPID_IMPOSSIBLE_SCENARY;
  if (seqnext == MCISIDV1_MAX_SEQUENCE) {
    if (sequence->lastime == instant)
      return MCISIDV1_OVERFLOW;
    return MCISIDV1_RESET;
  }
  sequence->lastime = instant;
  sequence->current = seqnext;
  return MCISIDV1_SUCCESS;
}

void mcisidv1ResetSequence(mcisidv1seq* sequence) {
  sequence->current = MCISIDV1_START_SEQUENCE;
}

mcisidv1snapshot mcisidv1TakeSnapshot(const mcisidv1seq* seq, int8_t model) {
  mcisidv1snapshot snapshot;
  snapshot.model = model;
  snapshot.instant = seq->lastime;
  snapshot.seqnext = seq->current;
  return snapshot;
}

mcisidstate mcisidv1Generate(mcisidv1seq* sequence, int8_t model, time_t instant, mcisidv1* output) {
  int initialStatus = mcisidv1NextValue(sequence, instant);
  if (initialStatus != MCISIDV1_SUCCESS)
    return initialStatus;
  mcisidv1snapshot snapshot = mcisidv1TakeSnapshot(sequence, model);
  mcisidv1WriteOutput(&snapshot, output);
  return MCISIDV1_SUCCESS;
}

void mcisidv1WriteOutput(const mcisidv1snapshot* snapshot, mcisidv1* output) {
  /* [IDENTIFIER: 6bits] */
  output->identifier = mcisidGetIdentifier(0);
  /* [MODEL: 6bits] */
  output->model = mcisidGetIdentifier(snapshot->model);
  /* [SEQUENCE: 18bits] */
  output->sequence[0] = mcisidGetIdentifier(snapshot->seqnext);
  output->sequence[1] = mcisidGetIdentifier((snapshot->seqnext >> 6));
  output->sequence[2] = mcisidGetIdentifier((snapshot->seqnext >> 12));
  /* [TIMESTAMP: 42bits] */
  output->timestamp[0] = mcisidGetIdentifier(snapshot->instant);
  output->timestamp[1] = mcisidGetIdentifier((snapshot->instant >> 6));
  output->timestamp[2] = mcisidGetIdentifier((snapshot->instant >> 12));
  output->timestamp[3] = mcisidGetIdentifier((snapshot->instant >> 18));
  output->timestamp[4] = mcisidGetIdentifier((snapshot->instant >> 24));
  output->timestamp[5] = mcisidGetIdentifier((snapshot->instant >> 30));
  output->timestamp[6] = mcisidGetIdentifier((snapshot->instant >> 36));
}

bool mcisidv1IsInvalid(const mcisidv1* mcisid) {
  return mcisidGetLookup(mcisid->sequence[0]) == 0;
}

int8_t mcisidv1GetOriginModel(const mcisidv1* input) {
  return mcisidGetLookup(input->model);
}

int32_t mcisidv1GetSequence(const mcisidv1* input) {
  int32_t s0 = mcisidGetLookup(input->sequence[0]);
  int32_t s1 = mcisidGetLookup(input->sequence[1]);
  int32_t s2 = mcisidGetLookup(input->sequence[2]);
  return (s2 << 12) | (s1 << 6) | s0;
}

time_t mcisidv1GetTimeInstantMilis(const mcisidv1* input) {
  time_t t0 = mcisidGetLookup(input->timestamp[0]);
  time_t t1 = mcisidGetLookup(input->timestamp[1]);
  time_t t2 = mcisidGetLookup(input->timestamp[2]);
  time_t t3 = mcisidGetLookup(input->timestamp[3]);
  time_t t4 = mcisidGetLookup(input->timestamp[4]);
  time_t t5 = mcisidGetLookup(input->timestamp[5]);
  time_t t6 = mcisidGetLookup(input->timestamp[6]);
  return (t6 << 36) | (t5 << 30) | (t4 << 24) | (t3 << 18) | (t2 << 12) | (t1 << 6) | t0;
}

mcisidv1cmp mcisidv1Compare(const mcisidv1* a, const mcisidv1* b) {
  if (a == NULL || b == NULL)
    return (mcisidv1cmp)MCISID_NULL_POINTER_ERROR;
  uint8_t aModel = mcisidv1GetOriginModel(a);
  uint8_t bModel = mcisidv1GetOriginModel(b);
  if (aModel > bModel)
    return (mcisidv1cmp)MCISIDV1_COMPARE_GREATER;
  else if (aModel < bModel)
    return (mcisidv1cmp)MCISIDV1_COMPARE_LESS;
  time_t aTimestamp = mcisidv1GetTimeInstantMilis(a);
  time_t bTimestamp = mcisidv1GetTimeInstantMilis(b);
  if (aTimestamp > bTimestamp)
    return (mcisidv1cmp)MCISIDV1_COMPARE_GREATER;
  else if (aTimestamp < bTimestamp)
    return (mcisidv1cmp)MCISIDV1_COMPARE_LESS;
  uint32_t aSequence = mcisidv1GetSequence(a);
  uint32_t bSequence = mcisidv1GetSequence(b);
  if (aSequence > bSequence)
    return (mcisidv1cmp)MCISIDV1_COMPARE_GREATER;
  else if (aSequence < bSequence)
    return (mcisidv1cmp)MCISIDV1_COMPARE_LESS;
  return (mcisidv1cmp)MCISIDV1_COMPARE_EQUAL;
}
