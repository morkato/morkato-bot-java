#ifndef MCISID_H
#define MCISID_H

#include <stdint.h>

#define MCISID_NULL_POINTER_ERROR 34
#define MCISID_NO_RESPONSE        33

#define MCISID_INVALID_CHARACTER 65

/*
 * Tipo que representa o estado de retorno das funções dentro de mcisid.
 * 0 - 32 Reservado para erros específicos dentro da função.
 * 33 - 64 Reservado para erros genéricos (toda a aplicação) dentro da função.
 */
typedef int mcisidstate;

void mcisidInit();
char mcisidGetIdentifier(uint8_t idx);
uint8_t mcisidGetVersionStrategy(const char* input);
uint8_t mcisidGetLookup(const char character);

#endif // MCISID_H