#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <time.h>
#include "mcisid.h"

#define MCISIDV1_RESET_SEQUENCE(seq) seq->current = MCISIDV1_START_SEQUENCE
#define MCISIDV1_NEXT_VALUE(seq) (seq->current + 1);

static const char IDENTIFIERS[65] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-";
static char LOOKUP[128];

char mcisidGetIdentifier(uint8_t idx) {
  return IDENTIFIERS[idx & MCISIDV1_IDENTIFER_MASK];
}

uint8_t mcisidGetLookup(char character) {
  return LOOKUP[(uint8_t)character];
}

__attribute__((constructor))
void mcisidInit() {
  memset(LOOKUP, MCISID_INVALID_CHARACTER, 128);
  for (char i = 0; i < 64; ++i) {
    LOOKUP[(uint8_t)mcisidGetIdentifier(i)] = i;
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
 * SEQUENCE   (24 bits) -> Incrementado para garantir unicidade dentro do mesmo segundo.
 *                         Reservado: 0 - 1024 (usado internamente).
 * TIMESTAMP  (36 bits) -> Tempo em segundos desde um User Defined Epoch (UDE).
 *                         Duração máxima estimada: ~2000 anos a partir do UDE.
 *
 * TAMANHO FIXO: 12 BYTES (72 BITS BRUTO)
 * ESTRUTURA BINÁRIA: [IDENTIFIER: 6b] [MODEL: 6b] [SEQUENCE: 24b] [TIMESTAMP: 36b]
 */
int mcisidv1Generate(char* output, mcisidv1gen* gen, uint8_t model) {
  if (gen->epoch == MCISIDV1_EPOCH_INVALID_VALUE)
    return MCISIDV1_NOT_INITIALIZED_EPOCH;
  mcisidv1seq* seq = gen->sequences + model;
  uint32_t instant = time(NULL) - gen->epoch;
  uint32_t seqnext = MCISIDV1_NEXT_VALUE(seq);
  if (seq->current >= MCISIDV1_MAX_SEQUENCE) {
    if (seq->lastime == instant)
      return MCISIDV1_OVERFLOW;
    return MCISIDV1_RESET;
  }
  seq->current = seqnext;
  seq->lastime = instant;
  
  // [IDENTIFIER: 6bits]
  output[0] = mcisidGetIdentifier(0);
  // [MODEL: 6bits]
  output[1] = mcisidGetIdentifier(model);
  // [SEQUENCE: 24bits]
  output[2] = mcisidGetIdentifier(seqnext);
  output[3] = mcisidGetIdentifier((seqnext >> 6));
  output[4] = mcisidGetIdentifier((seqnext >> 12));
  output[5] = mcisidGetIdentifier((seqnext >> 18));
  // [TIMESTAMP: 36bits]
  output[6] = mcisidGetIdentifier(instant);
  output[7] = mcisidGetIdentifier((instant >> 6));
  output[8] = mcisidGetIdentifier((instant >> 12));
  output[9] = mcisidGetIdentifier((instant >> 18));
  output[10] = mcisidGetIdentifier((instant >> 24));
  output[11] = mcisidGetIdentifier((instant >> 30));

  return MCISIDV1_SUCCESS;
}

uint8_t mcisidv1GetOriginModel(char* input) {
  return mcisidGetLookup(input[1]);
}

uint32_t mcisidv1GetSequence(char* input) {
  uint32_t s0 = mcisidGetLookup(input[2]);
  uint32_t s1 = mcisidGetLookup(input[3]);
  uint32_t s2 = mcisidGetLookup(input[4]);
  uint32_t s3 = mcisidGetLookup(input[5]);
  return (s3 << 18) | (s2 << 12) | (s1 << 6) | s0;
}

uint32_t mcisidv1GetTimeSeconds(char* input) {
  uint32_t t0 = mcisidGetLookup(input[6]);
  uint32_t t1 = mcisidGetLookup(input[7]);
  uint32_t t2 = mcisidGetLookup(input[8]);
  uint32_t t3 = mcisidGetLookup(input[9]);
  uint32_t t4 = mcisidGetLookup(input[10]);
  uint32_t t5 = mcisidGetLookup(input[11]);
  return (t5 << 30) | (t4 << 24) | (t3 << 18) | (t2 << 12) | (t1 << 6) | t0;
}

time_t mcisidv1CreatedAt(mcisidv1gen* generator, char* input) {
  return generator->epoch + mcisidv1GetTimeSeconds(input);
}