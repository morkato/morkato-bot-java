#ifndef MCISID_H
#define MCISID_H

#include <stdint.h>
#include <stdbool.h>

#define MCISIDV1_MAX_SEQUENCE 0xFFFFFF
#define MCISIDV1_SIZE 12
#define MCISIDV1_SUCCESS 0
#define MCISIDV1_OVERFLOW 1
#define MCISIDV1_RESET 2
#define MCISIDV1_LOCKED 3
#define MCISIDV1_NOT_INITIALIZED_EPOCH 3
#define MCISIDV1_START_SEQUENCE 1024
#define MCISIDV1_IDENTIFER_MASK 0x3F
#define MCISIDV1_MAX_SEQUENCES 64
#define MCISID_NO_RESPONSE 63
#define MCISID_INVALID_CHARACTER 65

#define MCISIDV1_EPOCH_INVALID_VALUE -1

typedef struct {
  uint32_t current;
  uint32_t lastime;
} mcisidv1seq;

typedef struct {
  mcisidv1seq* sequences;
  time_t epoch;
} mcisidv1gen;

uint8_t mcisidGetVersionStrategy(char* input);
uint8_t mcisidGetLookup(const char character);
char mcisidGetIdentifier(uint8_t idx);
void mcisidInit(void);
void mcisidv1BootGenerator(mcisidv1gen* generator, mcisidv1seq* sequences);
void mcisidv1ResetSequence(mcisidv1gen* generator, uint8_t model);
int mcisidv1Generate(char* output, mcisidv1gen* gen, uint8_t model);
time_t mcisidv1CreatedAt(mcisidv1gen* generator, char* input);
uint8_t mcisidv1GetOriginModel(char* input);
uint32_t mcisidv1GetSequence(char* input);
uint32_t mcisidv1GetTimeSeconds(char* input);

#endif // MCISID_H