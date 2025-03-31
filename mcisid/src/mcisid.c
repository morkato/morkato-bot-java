#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <time.h>
#include "mcisid.h"

#define MCISID_GETIDENTIFIER(idx) IDENTIFIERS[(uint8_t)(idx & MCISIDV1_IDENTIFER_MASK)]
#define MCISID_GETLOOKUP(character) LOOKUP[(uint8_t)character]
#define MCISIDV1_RESET_SEQUENCE(generator) generator->sequence = MCISIDV1_START_SEQUENCE
#define MCISIDV1_NEXT_VALUE(generator) ++generator->sequence;

static const char IDENTIFIERS[65] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-";
static char LOOKUP[128];
static time_t EPOCH = -1;
static bool SAFE_GENERATION = false;
static mcisidv1gen* generators = NULL;

__attribute__((constructor))
void mcisidInit() {
  memset(LOOKUP, MCISID_INVALID_CHARACTER, 128);
  for (char i = 0; i < 64; ++i) {
    LOOKUP[MCISID_GETIDENTIFIER(i)] = i;
  }
}

void mcisidv1SetGenerators(mcisidv1gen* _generators) {
  generators = _generators;
}

void mcisidv1SetSafeGeneration(bool generation) {
  SAFE_GENERATION = generation;
}

time_t mcisidv1GetEpoch() {
  return EPOCH;
}

int mcisidv1SetEpoch(time_t epoch) {
  if (epoch >= time(NULL))
    return MCISIDV1_OVERFLOW;
  EPOCH = epoch;
  return MCISIDV1_SUCCESS;
}

void mcisidCreate(mcisidv1gen* generator) {
  generator->sequence = MCISIDV1_START_SEQUENCE;
  generator->lastime = 0;
  generator->locked = false;
}

void mcisidv1Lock(uint8_t model) {
  mcisidv1gen* gen = generators + model;
  gen->locked = true;
}

void mcisidv1Unlock(uint8_t model) {
  mcisidv1gen* gen = generators + model;
  gen->locked = false;
}

uint8_t mcisidGetVersionStrategy(char* input) {
  return MCISID_GETLOOKUP(input[0]);
}

void mcisidv1ResetSequence(uint8_t model) {
  (generators + model)->sequence = MCISIDV1_START_SEQUENCE;
}

uint8_t mcisidGetLookup(const char character) {
  return MCISID_GETLOOKUP(character);
}

/* [IDENTIFIER: 6bits][MODEL: 6bits][SEQUENCE: 24bits][TIMESTAMP(S): 36bits] */
int mcisidv1Generate(char* output, uint8_t model) {
  if (generators == NULL || EPOCH == -1)
    return MCISIDV1_NOT_INITIALIZED;
  mcisidv1gen* gen = generators + model;
  if (gen->locked && SAFE_GENERATION)
    return MCISIDV1_LOCKED;
  uint32_t instant = time(NULL) - EPOCH;
  uint32_t seqnext = MCISIDV1_NEXT_VALUE(gen);
  if (seqnext >= MCISIDV1_MAX_SEQUENCE) {
    if (gen->lastime == instant)
      return MCISIDV1_OVERFLOW;
    return MCISIDV1_RESET;
  }
  gen->lastime = instant;
  
  // [IDENTIFIER: 6bits]
  output[0] = MCISID_GETIDENTIFIER(0);
  // [MODEL: 6bits]
  output[1] = MCISID_GETIDENTIFIER(model & MCISIDV1_IDENTIFER_MASK);
  // [SEQUENCE: 24bits]
  output[2] = MCISID_GETIDENTIFIER(seqnext & MCISIDV1_IDENTIFER_MASK);
  output[3] = MCISID_GETIDENTIFIER((seqnext >> 6) & MCISIDV1_IDENTIFER_MASK);
  output[4] = MCISID_GETIDENTIFIER((seqnext >> 12) & MCISIDV1_IDENTIFER_MASK);
  output[5] = MCISID_GETIDENTIFIER((seqnext >> 18) & MCISIDV1_IDENTIFER_MASK);
  // [TIMESTAMP: 36bits]
  output[6] = MCISID_GETIDENTIFIER(instant & MCISIDV1_IDENTIFER_MASK);
  output[7] = MCISID_GETIDENTIFIER((instant >> 6) & MCISIDV1_IDENTIFER_MASK);
  output[8] = MCISID_GETIDENTIFIER((instant >> 12) & MCISIDV1_IDENTIFER_MASK);
  output[9] = MCISID_GETIDENTIFIER((instant >> 18) & MCISIDV1_IDENTIFER_MASK);
  output[10] = MCISID_GETIDENTIFIER((instant >> 24) & MCISIDV1_IDENTIFER_MASK);
  output[11] = MCISID_GETIDENTIFIER((instant >> 30) & MCISIDV1_IDENTIFER_MASK);

  return MCISIDV1_SUCCESS;
}

uint8_t mcisidv1GetOriginModel(char* input) {
  return MCISID_GETLOOKUP(input[1]);
}

uint32_t mcisidv1GetSequence(char* input) {
  uint32_t s0 = MCISID_GETLOOKUP(input[2]);
  uint32_t s1 = MCISID_GETLOOKUP(input[3]);
  uint32_t s2 = MCISID_GETLOOKUP(input[4]);
  uint32_t s3 = MCISID_GETLOOKUP(input[5]);
  return (s3 << 18) | (s2 << 12) | (s1 << 6) | s0;
}

uint32_t mcisidv1GetTimeSeconds(char* input) {
  uint32_t t0 = MCISID_GETLOOKUP(input[6]);
  uint32_t t1 = MCISID_GETLOOKUP(input[7]);
  uint32_t t2 = MCISID_GETLOOKUP(input[8]);
  uint32_t t3 = MCISID_GETLOOKUP(input[9]);
  uint32_t t4 = MCISID_GETLOOKUP(input[10]);
  uint32_t t5 = MCISID_GETLOOKUP(input[11]);
  return (t5 << 30) | (t4 << 24) | (t3 << 18) | (t2 << 12) | (t1 << 6) | t0;
}