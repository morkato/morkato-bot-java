#include <time.h>
#include "mcisid/mcisidv1.h"
#include "mcisid/mcisid.h"

mcisidstate mcisidv1NextValue(mcisidv1seq* sequence, time_t instant) {
  if (instant > sequence->lastime)
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

void mcisidv1ResetSequence(mcisidv1seq* sequence, time_t instant) {
  sequence->current = MCISIDV1_START_SEQUENCE;
  sequence->lastime = instant;
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
  return output;
}