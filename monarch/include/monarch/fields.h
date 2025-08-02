#ifndef MCMCH_FIELDS_H
#define MCMCH_FIELDS_H
#include <stdint.h>

#include "monarch.h"

#define MCMCHFIELD_IOSIZE_NAME 32
#define MCMCHFIELD_IOSIZE_DESCRIPTION 2048

typedef char mcmchfield_name[MCMCHFIELD_IOSIZE_NAME];
typedef char mcmchfield_description[MCMCHFIELD_IOSIZE_DESCRIPTION];

_Static_assert(sizeof(mcmchfield_name) == MCMCHFIELD_IOSIZE_NAME, "O tamanho io do campo mcmchmodel_name deve ser igual a MCMCHFIELD_IOSIZE_NAME.");
_Static_assert(sizeof(mcmchfield_description) == MCMCHFIELD_IOSIZE_DESCRIPTION, "O tamanho io do campo mcmchmodel_description deve ser igual a MCMCHFIELD_IOSIZE_DESCRIPTION.");

mcmchstatus mcmchFieldNameIsByteValid(const char byte);
#endif // MCMCH_FIELDS_H