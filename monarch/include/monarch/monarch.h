#ifndef MONARCH_H
#define MONARCH_H
#include <mcisid/mcisidv1.h>
#include <mcisid/mcisid.h>
#include <string.h>
#include <stdint.h>

#define MCMCHSTATUS_SUCCESS           0
#define MCMCHSTATUS_OVERFLOW          1
#define MCMCHSTATUS_SPECIFIC          256

#define MCMCHSTATUS_NOT_ABSOLUTE_PATH 257
#define MCMCHSTATUS_PATH_NOT_EXISTS   258
#define MCMCHSTATUS_INVALID_PATH      259

#define MCMCH_MCISIDV1_SEQUENCE_SIZE  10
#define MCMCH_MCISIDV1_HASHTABLE_SIZE 32

#define MCMCHENV_SEQUENCES_LENGTH         64
#define MCMCHENV_SEQUENCES_MCISIDV1_SIZE  640
#define MCMCHENV_SEQUENCES_HASHTABLE_SIZE 2048

#define MCMCHENV_SETFLAG(env, flag) ((env)->flags |= (1 << flag))
#define MCMCHENV_CLEARFLAG(env, flag) ((env)->flags &= ~(1 << (flag)))
#define MCMCHENV_HASFLAG(env, flag) (((env)->flags & (1 << flag)) != 0)

#define MCMCHENV_FLAG_ROOTDIR_NOTFOUND 1
#define MCMCHENV_FLAG_ROOTDIR_SETTED   2
#define MCMCHENV_FLAG_LOGFILE_SETTED   3
#define MCMCHENV_FLAG_ONSYNC_IDSTORE   4

/* TODO: Aqui */
_Static_assert((MCMCHENV_SEQUENCES_LENGTH * MCMCH_MCISIDV1_SEQUENCE_SIZE) == MCMCHENV_SEQUENCES_MCISIDV1_SIZE, "Static error");
_Static_assert((MCMCHENV_SEQUENCES_LENGTH * MCMCH_MCISIDV1_HASHTABLE_SIZE) == MCMCHENV_SEQUENCES_HASHTABLE_SIZE, "Static error");
typedef uint8_t mcmchloglevel;
typedef uint32_t mcmchlogflags;
typedef uint32_t mcmchstatus;
typedef struct _mcmchenv_comm {
  char* rootdir;
  char* tmpdir;
  char* logfilepath;
  long epochInSeconds;
  mcisidv1seq sequences[MCMCHENV_SEQUENCES_LENGTH];
  uint64_t flags;
  mcmchloglevel loglevel;
} _mcmchenv_comm;
#if defined(__linux)
  typedef struct _mcmchenv {
    _mcmchenv_comm comm;
    int logfd;
    int idstorefd;
  } _mcmchenv;
#elif defined(__WIN32)
  #include <windows.h>
  typedef struct _mcmchenv {
    _mcmchenv_comm comm;
    HANDLE loghandle;
  } _mcmchenv;
#endif
typedef void* mcmchenv;

mcmchenv     mcmchInitilizeEnvironment ();
mcisidv1seq* mcmchGetSequence          (mcmchenv env, int16_t model);
void         mcmchNormalizeEnv         (mcmchenv env);
const char*  mcmchGetEnvRootDir        (const mcmchenv env);
const char*  mcmchGetLogFileName       (const mcmchenv env);
mcmchstatus  mcmchSetEnvRootDir        (mcmchenv env, char* rootdir);
mcmchstatus  mcmchSetEnvUserDir        (mcmchenv env, char* userdir);
mcmchstatus  mcmchSetEnvTmpDir         (mcmchenv env, char* tmpdir);
mcmchstatus  mcmchSetEnvLogFileName    (mcmchenv env, char* logfile);
mcmchstatus  mcmchSetEnvLogLevel       (mcmchenv env, mcmchloglevel level);
void         mcmchEnvClose             (mcmchenv env);
#endif // MONARCH_H