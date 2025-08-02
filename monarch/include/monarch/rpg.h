#ifndef MONARCH_RPG_H
#define MONARCH_RPG_H
#include <mcisid/mcisidv1.h>
#include <stdio.h>
#include "monarch.h"

typedef struct __attribute__((packed)) mcmchrpg {
  size_t players_count;
  mcisidv1 id;
} mcmchrpg;
#endif