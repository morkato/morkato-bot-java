#ifndef MONARCH_UTIL_H
#define MONARCH_UTIL_H
#include <stdio.h>

#if defined(__unix)
  #include <time.h>
  typedef time_t mcmchtime;
#elif defined(_WIN32)
  #include <windows.h>
  typedef _FILETIME mcmchtime;
#endif

mcmchtime mcmchCurrentTime ();
void      mcmchStrftime    (char* buffer, size_t maxlen, const char* format, const mcmchtime time);
#endif