#ifndef MONARCH_UID_H
#define MONARCH_UID_H
#include <mcisid/mcisidv1.h>
#include <openssl/sha.h>

#include "monarch.h"

#define MCMCHID_TABLE_FILENAME   "ids-store.mcisidv1"
#define MCMCHID_TABLE_START_SEQ  "1G00000000"
#define MCMCHID_SEQSIZE          10
#define MCMCHID_HASHSIZE         SHA256_DIGEST_LENGTH
#define MCMCHID_FILELENGTH_TOTAL 2688

typedef uint8_t mcmchid_mcmcisidv1_compact_seq[MCMCHID_SEQSIZE];
typedef uint8_t mcmchid_seqhash[MCMCHID_HASHSIZE];
typedef struct __attribute__((packed)) mcmchid_seqfile_payload {
  mcmchid_mcmcisidv1_compact_seq idseq;
  mcmchid_seqhash hash;
} mcmchid_seqfile_payload;

/* TODO: Aqui */
_Static_assert((sizeof(mcmchid_seqfile_payload) * MCMCHENV_SEQUENCES_LENGTH) == MCMCHID_FILELENGTH_TOTAL, "Static error");

int32_t     mcmchExtractCurrentSequenceValue  (const mcmchid_mcmcisidv1_compact_seq seq);
mcisidv1tm  mcmchExtractCurrentTimestampValue (const mcmchid_mcmcisidv1_compact_seq seq);
void        mcmchGenerateValidHash            (mcmchid_seqhash hasher, const mcmchid_mcmcisidv1_compact_seq seq);
int         mcmchValidateSequence             (const mcmchid_seqfile_payload* payload);
mcmchstatus mcmchOpenIdStoreFile              (const mcmchenv env, const char* filename);
mcmchstatus mcmchOpenIdStore                  (const mcmchenv env);
mcmchstatus mcmchCreateIdTableFile            (const mcmchenv env, const char* filename);
mcmchstatus mcmchCreateIdTable                (const mcmchenv env);
mcmchstatus mcmchCloseIdStore                 (mcmchenv env);

mcmchstatus mcmchNextId            (mcmchenv env, mcisidv1 mcisid, int8_t model);
mcmchstatus mcmchCompactSequenceId (mcmchid_seqfile_payload* compacted, const mcisidv1seq* sequence);
int         mcmchIsAvaibleIdStore  (mcmchenv env);
mcmchstatus mcmchSyncWithIdStore   (mcmchenv env, const mcisidv1_vmodel model, const mcmchid_seqfile_payload* payload);
#endif