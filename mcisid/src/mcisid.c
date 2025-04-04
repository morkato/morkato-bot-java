#define _POSIX_C_SOURCE 200809l
#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <sys/time.h>
#include "mcisid.h"

#define MCISIDV1_RESET_SEQUENCE(seq) seq->current = MCISIDV1_START_SEQUENCE
#define MCISIDV1_NEXT_VALUE(seq) (seq->current + 1);

static const char IDENTIFIERS[65] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-";
static char LOOKUP[128];

time_t getInstantInMilis() {
  struct timespec ts;
  clock_gettime(CLOCK_REALTIME, &ts);
  return (time_t)((ts.tv_sec * 1000) + (ts.tv_nsec / 1000000L));
}

char mcisidGetIdentifier(uint8_t idx) {
  return IDENTIFIERS[idx & MCISIDV1_IDENTIFER_MASK];
}

int8_t mcisidGetLookup(char character) {
  if ((uint8_t)character > 127)
    return MCISID_INVALID_CHARACTER;
  return LOOKUP[(uint8_t)character];
}

mcisidv1snapshot mcisidv1TakeSnapshot(const mcisidv1seq* seq, int8_t model) {
  mcisidv1snapshot snapshot;
  snapshot.model = model;
  snapshot.instant = seq->lastime;
  snapshot.seqnext = seq->current;
  return snapshot;
}

__attribute__((constructor))
void mcisidInit() {
  memset(LOOKUP, MCISID_INVALID_CHARACTER, 128);
  for (char i = 0; i < 64; ++i) {
    LOOKUP[(int8_t)mcisidGetIdentifier(i)] = i;
  }
}

void mcisidv1BootGenerator(mcisidv1gen* generator, mcisidv1seq* sequences) {
  generator->sequences = sequences;
  generator->epoch = MCISIDV1_EPOCH_INVALID_VALUE;
  for (uint8_t i = 0; i < MCISIDV1_MAX_SEQUENCES; ++i) {
    mcisidv1seq* seq = sequences + i;
    MCISIDV1_RESET_SEQUENCE(seq);
    seq->lastime = 0;
  }
}

void mcisidv1ResetSequence(mcisidv1gen* generator, uint8_t model) {
  MCISIDV1_RESET_SEQUENCE((generator->sequences + model));
}

/*
 * ESTRUTURA DO MCISIDV1 (VERSÃO 1)
 *
 * IDENTIFIER (6 bits)  -> Sempre 0 para a versão V1.
 * MODEL      (6 bits)  -> Indica o tipo/origem do objeto no banco. Não representa um nó.
 * SEQUENCE   (18 bits) -> Incrementado para garantir unicidade dentro do mesmo milisegundo.
 *                         Reservado: 0 - 1024 (usado internamente).
 * TIMESTAMP  (42 bits) -> Tempo em milisegundos desde um User Defined Epoch (UDE).
 *                         Duração máxima estimada: ~130 anos a partir do UDE.
 *
 * TAMANHO FIXO: 12 BYTES (72 BITS BRUTO)
 * ESTRUTURA BINÁRIA: [IDENTIFIER: 6b] [MODEL: 6b] [SEQUENCE: 18b] [TIMESTAMP: 42b]
 */
int mcisidv1Generate(mcisidv1* output, mcisidv1gen* gen, uint8_t model) {
  if (gen->epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    return MCISIDV1_NOT_INITIALIZED_EPOCH;
  mcisidv1seq* seq = gen->sequences + model;
  time_t instant = (time_t)(getInstantInMilis() - (gen->epoch * 1000));
  int32_t seqnext = MCISIDV1_NEXT_VALUE(seq);
  if (seq->current >= MCISIDV1_MAX_SEQUENCE) {
    if (seq->lastime < instant)
      return MCISIDV1_EPOCH_CORRUPTED;
    if (seq->lastime == instant)
      return MCISIDV1_OVERFLOW;
    return MCISIDV1_RESET;
  }
  mcisidv1snapshot snapshot = mcisidv1TakeSnapshot(seq, model);
  seq->current = seqnext;
  seq->lastime = instant;
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

bool mcisidv1IsInvalid(const mcisidv1* mcisid) {
  return mcisidv1GetSequence(mcisid) == MCISIDV1_INVALID_SEQVALUE;
}

int8_t mcisidv1GetOriginModel(mcisidv1* input) {
  return mcisidGetLookup(input->model);
}

int32_t mcisidv1GetSequence(mcisidv1* input) {
  uint32_t s0 = mcisidGetLookup(input->sequence[0]);
  uint32_t s1 = mcisidGetLookup(input->sequence[1]);
  uint32_t s2 = mcisidGetLookup(input->sequence[2]);
  return (s2 << 12) | (s1 << 6) | s0;
}

time_t mcisidv1GetTimeMilis(mcisidv1* input) {
  time_t t0 = mcisidGetLookup(input->timestamp[0]);
  time_t t1 = mcisidGetLookup(input->timestamp[1]);
  time_t t2 = mcisidGetLookup(input->timestamp[2]);
  time_t t3 = mcisidGetLookup(input->timestamp[3]);
  time_t t4 = mcisidGetLookup(input->timestamp[4]);
  time_t t5 = mcisidGetLookup(input->timestamp[5]);
  time_t t6 = mcisidGetLookup(input->timestamp[6]);
  return (t6 << 36) | (t5 << 30) | (t4 << 24) | (t3 << 18) | (t2 << 12) | (t1 << 6) | t0;
}

time_t mcisidv1CreatedAt(mcisidv1gen* generator, mcisidv1* input) {
  return (generator->epoch * 1000) + mcisidv1GetTimeMilis(input);
}

mcisidv1cmp mcisidv1Compare(mcisidv1* a, mcisidv1* b) {
  if (a == NULL || b == NULL)
    return (mcisidv1cmp)MCISID_NULL_POINTER_ERROR;
  uint8_t aModel = mcisidv1GetOriginModel(a);
  uint8_t bModel = mcisidv1GetOriginModel(b);
  if (aModel > bModel)
    return (mcisidv1cmp)MCISIDV1_COMPARE_GREATER;
  else if (aModel < bModel)
    return (mcisidv1cmp)MCISIDV1_COMPARE_LESS;
  uint32_t aTimestamp = mcisidv1GetTimeMilis(a);
  uint32_t bTimestamp = mcisidv1GetTimeMilis(b);
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