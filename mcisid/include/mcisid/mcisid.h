#ifndef MCISID_H
#define MCISID_H

#include <stdint.h>

#define MCISID_NULL_POINTER_ERROR 0x7F
#define MCISID_NO_RESPONSE        63

#define MCISID_INVALID_CHARACTER 65

/*
 * Tipo que representa o estado de retorno das funções dentro de mcisid.
 * 0 - 32 Reservado para erros específicos dentro da função.
 * 33 - 64 Reservado para erros genéricos (toda a aplicação) dentro da função.
 */
typedef int mcisidstate;

static const char* MCISID_IDENTIFIERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-";


inline uint8_t mcisidGetVersionStrategy(const char* input) {
  return mcisidGetLookup(input[0]);
}
inline char mcisidGetIdentifier(uint8_t idx) {
  if (idx > 63)
    return '\0';
  return MCISID_IDENTIFIERS[idx];
}
void mcisidInit();
uint8_t mcisidGetLookup(const char character);

#endif // MCISID_H