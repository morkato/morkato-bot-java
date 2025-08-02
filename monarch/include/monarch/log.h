#ifndef MONARCH_LOG_H
#define MONARCH_LOG_H
#include "monarch.h"

#define MCMCHLOG_TRACE 0
#define MCMCHLOG_DEBUG 1
#define MCMCHLOG_INFO  2
#define MCMCHLOG_WARN  3
#define MCMCHLOG_ERROR 4

#define MCMCHLOG_MAX_LINE_SIZE 4096

mcmchstatus mcmchOpenLogFile(mcmchenv env);
mcmchstatus mcmchCloseLogFile(mcmchenv env);
mcmchstatus mcmchlogWriteLine(const mcmchenv env, const mcmchloglevel level, const char* line);
mcmchstatus mcmchlogFormatted(const mcmchenv env, const mcmchloglevel level, const mcmchlogflags flags, const char* message);
mcmchstatus mcmchlog(const mcmchenv env, const mcmchloglevel level, const mcmchlogflags flags, const char* message, ...);
#endif // MONARCH_LOG_H