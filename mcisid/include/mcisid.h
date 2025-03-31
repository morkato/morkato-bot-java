#ifndef MCISID_H
#define MCISID_H

#include <stdint.h>
#include <stdbool.h>

#define MCISIDV1_SIZE 12
#define MCISIDV1_SUCCESS 0
#define MCISIDV1_OVERFLOW 1
#define MCISIDV1_RESET 2
#define MCISIDV1_LOCKED 3
#define MCISIDV1_NOT_INITIALIZED 3
#define MCISID_NO_RESPONSE 63
#define MCISID_INVALID_CHARACTER 65

typedef struct {
  uint32_t sequence;
  uint32_t lastime;
  bool locked;
} mcisidv1gen;

void mcisidInit(void);
void mcisidv1SetGenerators(mcisidv1gen* generators);
void mcisidv1SetSafeGeneration(bool generation);
int mcisidv1SetEpoch(int64_t epoch);
void mcisidCreate(mcisidv1gen* generator);
void mcisidv1Lock(uint8_t model);
void mcisidv1Unlock(uint8_t model);
void mcisidv1ResetSequence(uint8_t model);
int mcisidv1Generate(char* output, uint8_t model);
uint8_t mcisidGetLookup(const char character);
uint8_t mcisidv1GetOriginModel(char* input);
uint32_t mcisidv1GetSequence(char* input);
uint32_t mcisidv1GetTimeSeconds(char* input);

#endif // MCISID_H