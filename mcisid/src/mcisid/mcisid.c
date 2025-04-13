#include "mcisid/mcisid.h"
#include <string.h>
#include <stdint.h>

static char LOOKUP[128] = {MCISID_INVALID_CHARACTER};
static const char* MCISID_IDENTIFIERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-";

char mcisidGetIdentifier(uint8_t idx) {
  return MCISID_IDENTIFIERS[idx & 0x3F];
}

__attribute__((constructor)) void mcisidInit() {
  memset(LOOKUP, MCISID_INVALID_CHARACTER, 128);
  for (uint8_t i = 0; i < 64; ++i) {
    LOOKUP[(uint8_t)mcisidGetIdentifier(i)] = i;
  }
}

uint8_t mcisidGetVersionStrategy(const char* input) {
  return mcisidGetLookup(input[0]);
}

uint8_t mcisidGetLookup(const char character) {
  if ((uint8_t)character > 127)
    return MCISID_INVALID_CHARACTER;
  return (uint8_t)LOOKUP[(uint8_t)character];
}