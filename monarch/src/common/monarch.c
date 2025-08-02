#include <stdlib.h>

#include "monarch/monarch.h"
#include "monarch/uid.h"
#include "monarch/log.h"

const char* mcmchGetEnvRootDir(const mcmchenv env) {
  return ((const _mcmchenv_comm*)env)->rootdir;
}

const char* mcmchGetLogFileName(const mcmchenv env) {
  return ((const _mcmchenv_comm*)env)->logfilepath;
}

void mcmchNormalizeEnv(mcmchenv env) {
  _mcmchenv_comm* comm = (_mcmchenv_comm*)env;
  comm->rootdir = NULL;
  comm->tmpdir = NULL;
  comm->logfilepath = NULL;
  comm->epochInSeconds = 0;
  memset(comm->sequences, 0,  MCMCHENV_SEQUENCES_MCISIDV1_SIZE);
  comm->flags = 0;
  comm->loglevel = MCMCHLOG_INFO;
}

mcisidv1seq* mcmchGetSequence(mcmchenv env, int16_t model) {
  _mcmchenv_comm* _comm = (_mcmchenv_comm*)env;
  if (!MCMCHENV_HASFLAG(_comm, MCMCHENV_FLAG_ONSYNC_IDSTORE))
    return NULL;
  return _comm->sequences + model;
}

void mcmchEnvClose(mcmchenv env) {
  mcmchlog(env, MCMCHLOG_INFO, 0, "Closing environment context.");
  mcmchCloseIdStore(env);
  mcmchCloseLogFile(env);
  free(env);
}