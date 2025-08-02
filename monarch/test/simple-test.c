#ifdef __linux
#include <mcisid/mcisidv1.h>
#include <monarch/monarch.h>
#include <monarch/store.h>
#include <monarch/uid.h>
#include <monarch/log.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main() {
  printf("%ld\n", mcisidv1GetSystemInstant());
  const char* a = "00GG0CYY8Xj0";
  printf("%ld\n", mcisidv1GetTimeInstantMilis(a));
  const char* passedRootdir = getenv("MONARCH_ROOTDIR");
  const char* passedLogfilename = getenv("MONARCH_SAVE_LOGFILE");
  const char* pwd = getenv("PWD");
  if (passedRootdir == NULL || passedLogfilename == NULL) {
    fprintf(stderr, "It's necessary pass MONARCH_ROOTDIR and MONARCH_SAVE_LOGFILE environment, to configure application context.\n");
    return 1;
  }
  char rootdir[4096];
  char logfilename[4096];
  if (passedRootdir[0] == '/')
    memcpy(rootdir, passedRootdir, strlen(passedRootdir) + 1);
  else
    snprintf(rootdir, 4096, "%s/%s", pwd, passedRootdir);
  if (passedLogfilename[0] == '/')
    memcpy(logfilename, passedLogfilename, strlen(passedLogfilename) + 1);
  else
    snprintf(logfilename, 4096, "%s/%s", pwd, passedLogfilename);
  mcmchenv env = mcmchInitilizeEnvironment();
  int16_t i = 0;
  mcmchSetEnvRootDir(env, rootdir);
  mcmchSetEnvLogFileName(env, logfilename);
  mcmchOpenLogFile(env);
  mcmchSetEnvLogLevel(env, MCMCHLOG_DEBUG);
  // mcmchGenerateStore(env);
  mcmchOpenIdStore(env);
  mcisidv1seq* seq = mcmchGetSequence(env, i);
  if (seq == NULL)
    printf("Nenhuma sequência carregada!\n");
  else
    printf("(%hd) Sequence Current: %i;\n(%hd) Last Time: %ld;\n", i, seq->current, i, seq->lastime);
  mcisidv1 mcisid;
  if (mcmchNextId(env, mcisid, (int8_t)0) != MCMCHSTATUS_SUCCESS) {
    printf("An error\n");
    mcmchEnvClose(env);  
    return 1;
  }
  printf("Generated ID: ");
  for (uint8_t i = 0; i < MCISIDV1_IOSIZE; ++i)
    putchar(mcisid[i]);
  putchar('\n');
  mcmchEnvClose(env);
  return 0;
}
#endif