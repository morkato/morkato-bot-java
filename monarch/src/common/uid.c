#include <mcisid/mcisid.h>
#include <openssl/sha.h>
#include <stdio.h>
#include <string.h>

#include "monarch/monarch.h"
#include "monarch/uid.h"
#include "monarch/log.h"

int32_t mcmchExtractCurrentSequenceValue(const mcmchid_mcmcisidv1_compact_seq seq) {
  int32_t seq0 = mcisidGetLookup((const char)seq[0]);
  int32_t seq1 = mcisidGetLookup((const char)seq[1]);
  int32_t seq2 = mcisidGetLookup((const char)seq[2]);
  return (seq2 << 12) | (seq1 << 6) | seq0;
}

mcisidv1tm mcmchExtractCurrentTimestampValue(const mcmchid_mcmcisidv1_compact_seq seq) {
  mcisidv1tm t0 = mcisidGetLookup((const char)seq[3]);
  mcisidv1tm t1 = mcisidGetLookup((const char)seq[4]);
  mcisidv1tm t2 = mcisidGetLookup((const char)seq[5]);
  mcisidv1tm t3 = mcisidGetLookup((const char)seq[6]);
  mcisidv1tm t4 = mcisidGetLookup((const char)seq[7]);
  mcisidv1tm t5 = mcisidGetLookup((const char)seq[8]);
  mcisidv1tm t6 = mcisidGetLookup((const char)seq[9]);
  return (t6 << 36) | (t5 << 30) | (t4 << 24) | (t3 << 18) | (t2 << 12) | (t1 << 6) | t0;
}

mcmchstatus mcmchNextId(mcmchenv env, mcisidv1 mcisid, int8_t model) {
  _mcmchenv_comm* _comm = (_mcmchenv_comm*)env;
  if (!MCMCHENV_HASFLAG(_comm, MCMCHENV_FLAG_ONSYNC_IDSTORE) || !mcmchIsAvaibleIdStore(env))
    return MCMCHSTATUS_SPECIFIC;
  mcisidv1tm instant = mcisidv1GetSystemInstant() - (_comm->epochInSeconds * 1000L);
  mcisidstate state = MCISID_NO_RESPONSE;
  mcmchid_seqfile_payload compacted;
  mcisidv1seq* commited = _comm->sequences + (model & 0x3F);
  mcisidv1seq copy;
  memcpy(&copy, commited, sizeof(mcisidv1seq));
  state = mcisidv1NextValue(&copy, instant);
  for (uint8_t tries = 0; tries < 10; ++tries) {
    if (state == MCISIDV1_SUCCESS)
      break;
  }
  if (state != MCISIDV1_SUCCESS) {
    mcmchlog(env, MCMCHLOG_INFO, 0, "Error on generate mcisidv1.");
    return MCMCHSTATUS_SPECIFIC;
  }
  mcmchCompactSequenceId(&compacted, &copy);
  mcisidv1snapshot shot = mcisidv1TakeSnapshot(&copy, (model & 0x3F));
  if (mcmchSyncWithIdStore(env, model, &compacted) != MCMCHSTATUS_SUCCESS)
    return MCMCHSTATUS_SPECIFIC;
  commited->current = copy.current;
  commited->lastime = copy.lastime;
  mcisidv1WriteOutput(&shot, mcisid);
  return MCMCHSTATUS_SUCCESS;
}

void mcmchGenerateValidHash(mcmchid_seqhash hasher, const mcmchid_mcmcisidv1_compact_seq seq) {
  SHA256(seq, MCMCHID_SEQSIZE, hasher);
}

int mcmchValidateSequence(const mcmchid_seqfile_payload* payload) {
  mcmchid_seqhash otherhash;
  mcmchGenerateValidHash(otherhash, payload->idseq);
  return memcmp(payload->hash, otherhash, MCMCHID_HASHSIZE) == 0;
}

mcmchstatus mcmchCompactSequenceId(mcmchid_seqfile_payload* compacted, const mcisidv1seq* sequence) {
  compacted->idseq[0] = (uint8_t)mcisidGetIdentifier(sequence->current & 0x3F);
  compacted->idseq[1] = (uint8_t)mcisidGetIdentifier((sequence->current >> 6) & 0x3F);
  compacted->idseq[2] = (uint8_t)mcisidGetIdentifier((sequence->current >> 12) & 0x3F);
  compacted->idseq[3] = (uint8_t)mcisidGetIdentifier(sequence->lastime & 0x3F);
  compacted->idseq[4] = (uint8_t)mcisidGetIdentifier((sequence->lastime >> 6) & 0x3F);
  compacted->idseq[5] = (uint8_t)mcisidGetIdentifier((sequence->lastime >> 12) & 0x3F);
  compacted->idseq[6] = (uint8_t)mcisidGetIdentifier((sequence->lastime >> 18) & 0x3F);
  compacted->idseq[7] = (uint8_t)mcisidGetIdentifier((sequence->lastime >> 24) & 0x3F);
  compacted->idseq[8] = (uint8_t)mcisidGetIdentifier((sequence->lastime >> 30) & 0x3F);
  compacted->idseq[9] = (uint8_t)mcisidGetIdentifier((sequence->lastime >> 36) & 0x3F);
  mcmchGenerateValidHash(compacted->hash, compacted->idseq);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchOpenIdStore(const mcmchenv env) {
  const char* rootdir = mcmchGetEnvRootDir(env);
  size_t rootdir_length = strlen(rootdir);
  size_t filename_length = strlen(MCMCHID_TABLE_FILENAME);
  size_t length = rootdir_length + filename_length + 1;
  char abspath[length + 1];
  abspath[length] = '\0';
  memcpy(abspath, rootdir, rootdir_length);
  memcpy(abspath + rootdir_length, "/", 1);
  memcpy(abspath + rootdir_length + 1, MCMCHID_TABLE_FILENAME, filename_length);
  mcmchlog(env, MCMCHLOG_INFO, 0, "Opening id table store in: \"%s\".", abspath);
  return mcmchOpenIdStoreFile(env, abspath);
}

mcmchstatus mcmchCreateIdTable(const mcmchenv env) {
  const char* rootdir = mcmchGetEnvRootDir(env);
  size_t rootdir_length = strlen(rootdir);
  size_t filename_length = strlen(MCMCHID_TABLE_FILENAME);
  size_t length = rootdir_length + filename_length + 1;
  char abspath[length + 1];
  abspath[length] = '\0';
  memcpy(abspath, rootdir, rootdir_length);
  memcpy(abspath + rootdir_length, "/", 1);
  memcpy(abspath + rootdir_length + 1, MCMCHID_TABLE_FILENAME, filename_length);
  mcmchlog(env, MCMCHLOG_INFO, 0, "Creating id table store in: \"%s\".", abspath);
  return mcmchCreateIdTableFile(env, (const char*)abspath);
}