#ifndef MONARCH_STORE_H
#define MONARCH_STORE_H
#include "monarch.h"

#define MCMCHSTORE_ERROR_ROOTDIR_NOT_SETTED         257
#define MCMCHSTORE_ERROR_ROOTDIR_DIR_ALREADY_EXISTS 258
#define MCMCHSTORE_ERROR_CREATING_DIR               259

mcmchstatus mcmchGenerateStore(const mcmchenv env);
#endif // MONARCH_STORE_H