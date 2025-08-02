#ifndef MONARCH_ARTS_H
#define MONARCH_ARTS_H
#include <mcisid/mcisidv1.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>

#include "entity_context.h"
#include "monarch.h"
#include "fields.h"

#define MCMCHART_GLOBAL_FILENAME_OLD "arts.dat-old"
#define MCMCHART_GLOBAL_FILENAME "arts.dat"
#define MCMCHART_MODELSIZE 2108

#define MCMCHART_TYPE_RESPIRATION    0
#define MCMCHART_TYPE_KEKKIJUTSU     1
#define MCMCHART_TYPE_FIGHTING_STYLE 2

#define MCMCHART_WALTHER_RESPIRATION 0x1
#define MCMCHART_BREATH_RESPIRATION  0x2

typedef uint8_t mcmchart_userdefined_slot;
typedef struct __attribute__((packed)) mcmchart {
  mcisidv1 id;
  mcisidv1 ownerid;
  mcmchfield_name name;
  uint32_t artflags;
  mcmchfield_description description;
} mcmchart;

_Static_assert((sizeof(mcisidv1) * 2
              + sizeof(mcmchfield_name)
              + sizeof(uint32_t)
              + sizeof(mcmchfield_description)) == MCMCHART_MODELSIZE, "A operação deu errado, revise o macro: MCMCHART_MODELSIZE.");
_Static_assert(sizeof(mcmchart) == MCMCHART_MODELSIZE, "A arte não tem um tamanho estático de MCMCHART_MODELSIZE.");

int mcmchArtIsMatchId(const mcisidv1 mcisid);
void mcmchArtPrintRepr(const mcmchart* art);
void mcmchArtInitialize(const mcmchenv env, mcmchentity_context* art);
mcmchstatus mcmchArtValidateFields(mcmchentity_context* ctx);
mcmchstatus mcmchArtValidateEnvironment(const mcmchenv env, const mcmchart* art);
mcmchstatus mcmchArtGetAvaibleSlot(const mcmchenv env, mcmchart_userdefined_slot* slot);
mcmchstatus mcmchArtReflect(const mcmchenv env, mcmchart_userdefined_slot slot, mcmchart* art);
mcmchstatus mcmchArtSet(const mcmchenv env, mcmchart_userdefined_slot slot, mcmchart* art);
mcmchstatus mcmchArtMarkSlotAsUsed(const mcmchenv env, mcmchart_userdefined_slot slot);
mcmchstatus mcmchArtOverride(const mcmchenv env, mcmchart* art);
mcmchart mcmchArtSlotDrop(const mcmchenv env, const mcmchart_userdefined_slot slot);
mcmchart mcmchArtDrop(const mcmchenv env, const mcisidv1 id);

mcmchstatus mcmchArtSetId(mcmchentity_context* ctx, const mcisidv1 id);
mcmchstatus mcmchArtSetOwnerId(mcmchentity_context* ctx, const mcisidv1 ownerid);
mcmchstatus mcmchArtSetName(mcmchentity_context* ctx, const char* name);
mcmchstatus mcmchArtSetType(mcmchentity_context* ctx, const uint8_t* type);
mcmchstatus mcmchArtSetDescription(mcmchentity_context* ctx, const char* description);
#endif // MONARCH_ARTS_H