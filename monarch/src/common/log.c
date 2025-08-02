#include <stdarg.h>

#include "monarch/util.h"
#include "monarch/log.h"

static const char* loglevels[] = {"TRACE", "DEBUG", "INFO ", "WARN ", "ERROR"};

mcmchstatus mcmchlogFormatted(const mcmchenv env, const mcmchloglevel level, const mcmchlogflags flags, const char* message) {
  const mcmchtime now = mcmchCurrentTime();
  char formatted_time[64];
  char fullmessage[MCMCHLOG_MAX_LINE_SIZE];
  mcmchStrftime(formatted_time, sizeof(formatted_time), "%Y/%m/%dT%H:%M:%S", now);
  snprintf(fullmessage, MCMCHLOG_MAX_LINE_SIZE, "[%s] [%s] : %s\n", formatted_time, loglevels[level], message);
  return mcmchlogWriteLine(env, level, fullmessage);
}

mcmchstatus mcmchlog(const mcmchenv env, const mcmchloglevel level, const mcmchlogflags flags, const char* message, ...) {
  char formattedmessage[MCMCHLOG_MAX_LINE_SIZE - 100];
  va_list args;
  va_start(args, message);
  vsnprintf(formattedmessage, sizeof(formattedmessage), message, args);
  va_end(args);
  return mcmchlogFormatted(env, level, flags, formattedmessage);
}