#ifdef __linux
#include <sys/stat.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <malloc.h>

#include "monarch/monarch.h"
#include "monarch/log.h"

mcmchenv mcmchInitilizeEnvironment() {
  _mcmchenv* env = (_mcmchenv*)malloc(sizeof(_mcmchenv));
  _mcmchenv_comm* comm = (_mcmchenv_comm*)env;
  mcmchNormalizeEnv((mcmchenv)comm);
  env->logfd = -1;
  env->idstorefd = -1;
  return (mcmchenv)env;
}

mcmchstatus mcmchSetEnvRootDir(mcmchenv env, char* rootdir) {
  _mcmchenv_comm* _env = (_mcmchenv_comm*)env;
  struct stat st;
  if (rootdir[0] != '/')
    return MCMCHSTATUS_NOT_ABSOLUTE_PATH;
  else if (stat(rootdir, &st) != 0)
    MCMCHENV_SETFLAG(_env, MCMCHENV_FLAG_ROOTDIR_NOTFOUND);
  else if (!S_ISDIR(st.st_mode))
    return MCMCHSTATUS_INVALID_PATH;
  _env->rootdir = rootdir;
  MCMCHENV_SETFLAG(_env, MCMCHENV_FLAG_ROOTDIR_SETTED);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchSetEnvTmpDir(mcmchenv env, char* tmpdir) {
  _mcmchenv_comm* _env = (_mcmchenv_comm*)env;
  struct stat st;
  if (tmpdir[0] != '/')
    return MCMCHSTATUS_NOT_ABSOLUTE_PATH;
  else if (stat(tmpdir, &st) != 0)
    return MCMCHSTATUS_PATH_NOT_EXISTS;
  else if (!S_ISDIR(st.st_mode))
    return MCMCHSTATUS_INVALID_PATH;
  _env->tmpdir = tmpdir;
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchSetEnvLogFileName(mcmchenv env, char* logfile) {
  _mcmchenv_comm* _env = (_mcmchenv_comm*)env;
  if (logfile[0] != '/')
    return MCMCHSTATUS_NOT_ABSOLUTE_PATH;
  _env->logfilepath = logfile;
  MCMCHENV_SETFLAG(_env, MCMCHENV_FLAG_LOGFILE_SETTED);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchSetEnvLogLevel(mcmchenv env, mcmchloglevel level) {
  ((_mcmchenv_comm*)env)->loglevel = level;
  return MCMCHSTATUS_SUCCESS;
}
#endif