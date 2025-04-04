#include <string.h>
#include "mcisid/mcisid.h"

static char LOOKUP[128] = {MCISID_INVALID_CHARACTER};

__attribute__((constructor)) void mcisidInit() {
  memset(LOOKUP, MCISID_INVALID_CHARACTER, 128);
  for (uint8_t i = 0; i < 64; ++i) {
    LOOKUP[mcisidGetIdentifier(i)] = i;
  }
}

uint8_t mcisidGetLookup(const char character) {
  if (character > 127)
    return MCISID_INVALID_CHARACTER;
  return (uint8_t)LOOKUP[(const uint8_t)character];
}