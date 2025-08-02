#ifdef __linux
#include <mcisid/mcisidv1.h>
#include <mcisid/mcisid.h>
#include <openssl/sha.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <string.h>
#include <stdio.h>
#include <stdint.h>

#include "monarch/store.h"
#include "monarch/uid.h"

mcmchstatus mcmchGenerateStore(const mcmchenv env) {
  if (!MCMCHENV_HASFLAG((_mcmchenv_comm*)env, MCMCHENV_FLAG_ROOTDIR_SETTED))
    return MCMCHSTORE_ERROR_ROOTDIR_NOT_SETTED;
  if (!MCMCHENV_HASFLAG((_mcmchenv_comm*)env, MCMCHENV_FLAG_ROOTDIR_NOTFOUND))
    return MCMCHSTORE_ERROR_ROOTDIR_DIR_ALREADY_EXISTS;
  if (mkdir(mcmchGetEnvRootDir(env), 0755) != 0)
    return MCMCHSTORE_ERROR_CREATING_DIR;
  mcmchCreateIdTable(env);
  return MCMCHSTATUS_SUCCESS;
}
#endif