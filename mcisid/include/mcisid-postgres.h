#ifndef MCISID_UTILS_POSTGRES_H
#define MCISID_UTILS_POSTGRES_H

#include "postgres.h"
#include <stdint.h>
#include <time.h>

int32_t pgmcnextval(Oid relid, uint8_t model, time_t* instant);

#endif // MCISID_UTILS_POSTGRES_H