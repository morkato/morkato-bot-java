#include <string.h>

#include "monarch/monarch.h"
#include "monarch/fields.h"
#include "monarch/arts.h"

void mcmchArtPrintRepr(const mcmchart* art) {
  char name[MCMCHFIELD_IOSIZE_NAME + 1] = {0};
  char id[MCISIDV1_IOSIZE + 1];
  char ownerid[MCISIDV1_IOSIZE + 1];
  id[MCISIDV1_IOSIZE] = '\0';
  ownerid[MCISIDV1_IOSIZE] = '\0';
  memcpy(name, art->name, MCMCHFIELD_IOSIZE_NAME);
  memcpy(id, art->id, MCISIDV1_IOSIZE);
  memcpy(ownerid, art->ownerid, MCISIDV1_IOSIZE);
  printf("Art[name=%s, id=%s, ownerid=%s]\n", name, id, ownerid);
}

// mcmchstatus mcmchArtValidateFields(const mcmchart* art);
// mcmchstatus mcmchArtValidateEnvironment(const mcmchenv env, const mcmchart* art);
// mcmchstatus mcmchArtGetAvaibleSlot(const mcmchenv env, mcmchart_userdefined_slot* slot);
// mcmchstatus mcmchArtReflect(const mcmchenv env, mcmchart_userdefined_slot slot, mcmchart* art);
// mcmchstatus mcmchArtSet(const mcmchenv env, mcmchart_userdefined_slot slot, mcmchart* art);
// mcmchstatus mcmchArtMarkSlotAsUsed(const mcmchenv env, mcmchart_userdefined_slot slot);
// mcmchstatus mcmchArtOverride(const mcmchenv env, mcmchart* art);
// mcmchstatus mcmchArtSlotDrop(const mcmchenv env, const mcmchart_userdefined_slot slot);
// mcmchstatus mcmchArtDrop(const mcmchenv env, const mcisidv1 id);

int mcmchFieldNameValidate(mcmchfield_name name) {
  return 0;
}

void mcmchArtInitialize(const mcmchenv env, mcmchentity_context* ctx) {
  mcmchart* art = (mcmchart*)ctx->entity;
  memset(art->name, 0, MCMCHFIELD_IOSIZE_NAME);
  memcpy(art->ownerid, MCISIDV1_INVALID_ID, MCISIDV1_IOSIZE);
  memcpy(art->id, MCISIDV1_INVALID_ID, MCISIDV1_IOSIZE);
  memset(art->description, 0, MCMCHFIELD_IOSIZE_DESCRIPTION);
  art->artflags = 0;
  ctx->user_flags = 0;
  MCMCHENTITY_CONTEXT_MARK_FLAG(ctx, MCMCHENTITY_CONTEXT_NORMALIZED_FLAG);
}

int mcmchArtIsMatchId(const mcisidv1 mcisid) {
  int8_t origin = mcisidv1GetOriginModel(mcisid);
  return (origin == MCMCHART_TYPE_RESPIRATION)
        || (origin == MCMCHART_TYPE_KEKKIJUTSU)
        || (origin == MCMCHART_TYPE_FIGHTING_STYLE);
}

mcmchstatus mcmchArtValidateFields(mcmchentity_context* ctx) {
  mcmchart* art = (mcmchart*)ctx->entity;
  if (!MCMCHENTITY_CONTEXT_NORMALIZED(ctx)
    || mcisidv1IsInvalid(art->id)
    || mcisidv1IsInvalid(art->ownerid)
  ) return MCMCHSTATUS_SPECIFIC;
  if (!mcmchArtIsMatchId(art->id))
    /* TODO: a */
    return MCMCHSTATUS_SPECIFIC;
  MCMCHENTITY_CONTEXT_MARK_FLAG(ctx, MCMCHENTITY_CONTEXT_VALIDATED_FLAG);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchArtSetId(mcmchentity_context* ctx, const mcisidv1 id) {
  memcpy(((mcmchart*)ctx->entity)->id, id, MCISIDV1_IOSIZE);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchArtSetOwnerId(mcmchentity_context* ctx, const mcisidv1 ownerid) {
  memcpy(((mcmchart*)ctx->entity)->ownerid, ownerid, MCISIDV1_IOSIZE);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchArtSetName(mcmchentity_context* ctx, const char* name) {
  size_t length = strlen(name);
  if (length >= MCMCHFIELD_IOSIZE_NAME)
    return MCMCHSTATUS_OVERFLOW;
  if (length != MCMCHFIELD_IOSIZE_NAME)
    ((mcmchart*)ctx->entity)->name[length] = '\0';
  memcpy(((mcmchart*)ctx->entity)->name, name, length);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchArtSetDescription(mcmchentity_context* ctx, const char* description) {
  size_t length = strlen(description);
  if (length >= MCMCHFIELD_IOSIZE_DESCRIPTION)
    return MCMCHSTATUS_OVERFLOW;
  if (length != MCMCHFIELD_IOSIZE_DESCRIPTION)
    ((mcmchart*)ctx->entity)->description[length] = '\0';
  memcpy(((mcmchart*)ctx->entity)->description, description, length);
  return MCMCHSTATUS_SUCCESS;
}