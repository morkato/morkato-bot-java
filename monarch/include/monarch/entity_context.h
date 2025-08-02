#ifndef MCMCH_ENTITY_CONTEXT_H
#define MCMCH_ENTITY_CONTEXT_H
#include <stdint.h>
#include <stdio.h>
#include <malloc.h>

#include "monarch.h"

#define MCMCHENTITY_CONTEXT_HAS_FLAG(ctx, flag) (((ctx)->internal_flags & (1ULL << (flag))) != 0)
#define MCMCHENTITY_CONTEXT_MARK_FLAG(ctx, flag) ((ctx)->internal_flags |= (1ULL << (flag)))

#define MCMCHENTITY_CONTEXT_NORMALIZED_FLAG 1
#define MCMCHENTITY_CONTEXT_VALIDATED_FLAG  2

#define MCMCHENTITY_CONTEXT_NORMALIZED(ctx) MCMCHENTITY_CONTEXT_HAS_FLAG(ctx, MCMCHENTITY_CONTEXT_NORMALIZED_FLAG)
#define MCMCHENTITY_CONTEXT_VALIDATED(ctx)  MCMCHENTITY_CONTEXT_HAS_FLAG(ctx,  MCMCHENTITY_CONTEXT_VALIDATED_FLAG)

typedef uint64_t mcmchentity_flags;
typedef struct mcmchentity_context {
  void* entity;
  size_t length;
  mcmchentity_flags internal_flags;
  mcmchentity_flags user_flags;
} mcmchentity_context;

mcmchstatus mcmchEntityOpen(mcmchentity_context* ctx, void* entity, size_t length);
mcmchstatus mcmchEntityAlloc(mcmchentity_context* ctx, size_t length);
mcmchstatus mcmchEntityFree(mcmchentity_context* ctx);
#endif