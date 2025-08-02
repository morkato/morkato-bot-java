#include "monarch/entity_context.h"

mcmchstatus mcmchEntityOpen(mcmchentity_context* ctx, void* entity, size_t length) {
  if (entity == NULL)
    return 1;
  ctx->entity = entity;
  ctx->length = length;
  ctx->internal_flags = 0;
  ctx->user_flags = 0;
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchEntityAlloc(mcmchentity_context* ctx, size_t length) {
  void* entity = malloc(length);
  return mcmchEntityOpen(ctx, entity, length);
}

mcmchstatus mcmchEntityFree(mcmchentity_context* ctx) {
  free(ctx->entity);
  return MCMCHSTATUS_SUCCESS;
}