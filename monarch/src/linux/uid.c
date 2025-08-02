#ifdef __linux
#define _POSIX_C_SOURCE 200809L 
#include <mcisid/mcisidv1.h>
#include <openssl/sha.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <malloc.h>
#include <time.h> 

#include "monarch/monarch.h"
#include "monarch/uid.h"
#include "monarch/log.h"

time_t getInstantInMilis() {
  struct timespec ts;
  clock_gettime(CLOCK_REALTIME, &ts);
  return (ts.tv_sec * 1000L) + (time_t)(ts.tv_nsec / 1000000L);
}

mcmchstatus mcmchCreateIdTableFile(const mcmchenv env, const char* filename) {
  int fd = open(filename, O_CREAT | O_WRONLY | O_APPEND, 0644);
  if (fd == -1)
    return MCMCHSTATUS_SPECIFIC;
  mcmchid_seqfile_payload payload;
  memcpy(payload.idseq, MCMCHID_TABLE_START_SEQ, MCMCHID_SEQSIZE);
  mcmchGenerateValidHash(payload.hash, payload.idseq);
  char buffer[MCMCHID_FILELENGTH_TOTAL];
  for (size_t i = 0; i < MCMCHENV_SEQUENCES_LENGTH; ++i) {
    size_t offset = sizeof(mcmchid_seqfile_payload) * i;
    memcpy(buffer + offset, &payload, sizeof(mcmchid_seqfile_payload));
  }
  ssize_t written = write(fd, buffer, MCMCHID_FILELENGTH_TOTAL);
  if (written < 0 || written != MCMCHID_FILELENGTH_TOTAL) {
    close(fd);
    return MCMCHSTATUS_SPECIFIC;
  }
  fsync(fd);
  close(fd);
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchOpenIdStoreFile(const mcmchenv env, const char* filename) {
  _mcmchenv* _env = (_mcmchenv*)env;
  _mcmchenv_comm* _comm = (_mcmchenv_comm*)env;
  int fd = open(filename, O_RDWR);
  if (fd == -1) {
    mcmchlog(env, MCMCHLOG_ERROR, 0, "Erro ao tentar abrir o \"id-store\" (open/posix).");
    return MCMCHSTATUS_SPECIFIC;
  }
  struct stat st;
  mcmchid_seqfile_payload payload;
  if (fstat(fd, &st) != 0) {
    mcmchlog(env, MCMCHLOG_ERROR, 0, "Erro ao tentar ler as informações do \"id-store\" (fstat).");
    close(fd);
    return MCMCHSTATUS_SPECIFIC;
  } else if (st.st_size != MCMCHID_FILELENGTH_TOTAL) {
    mcmchlog(env, MCMCHLOG_ERROR, 0, "O \"id-store\" está com um tamanho indefinido. Tente recuperar na varredura.");
    close(fd);
    return MCMCHSTATUS_SPECIFIC;
  }
  for (size_t i = 0; i < MCMCHENV_SEQUENCES_LENGTH; ++i) {
    ssize_t readed = read(fd, &payload, sizeof(mcmchid_seqfile_payload));
    if (readed < 0 || readed != sizeof(mcmchid_seqfile_payload)) {
      mcmchlog(env, MCMCHLOG_ERROR, 0, "Falha ao ler o conteúdo escrito em: %lu no \"id-store\".", i);
      close(fd);
      return MCMCHSTATUS_SPECIFIC;
    } else if (!mcmchValidateSequence(&payload)) {
      mcmchlog(env, MCMCHLOG_ERROR, 0, "Os dados podem estarem corrompidos, ou terem sidos alterados manualmente (id-store).");
      close(fd);
      return MCMCHSTATUS_SPECIFIC;
    }
    mcisidv1seq* seq = (_comm->sequences + i);
    seq->current = mcmchExtractCurrentSequenceValue(payload.idseq);
    seq->lastime = mcmchExtractCurrentTimestampValue(payload.idseq);
  }
  lseek(fd, 0, SEEK_SET);
  _env->idstorefd = fd;
  MCMCHENV_SETFLAG(_comm, MCMCHENV_FLAG_ONSYNC_IDSTORE);
  mcmchlog(env, MCMCHLOG_INFO, 0, "O IDSTORE foi carregado com sucesso, e está sincronizado.");
  return MCMCHSTATUS_SUCCESS;
}

int mcmchIsAvaibleIdStore(mcmchenv env) {
  return ((_mcmchenv*)env)->idstorefd != -1;
}

mcmchstatus mcmchSyncWithIdStore(mcmchenv env, const mcisidv1_vmodel model, const mcmchid_seqfile_payload* payload) {
  const _mcmchenv* _env = (_mcmchenv*)env;
  int fd = _env->idstorefd;
  lseek(fd, (model & 0x3F) * sizeof(mcmchid_seqfile_payload), SEEK_SET);
  ssize_t written = write(fd, payload, sizeof(mcmchid_seqfile_payload));
  fsync(fd);
  if (written < 0 || written != sizeof(mcmchid_seqfile_payload))
    return MCMCHSTATUS_SPECIFIC;
  return MCMCHSTATUS_SUCCESS;
}

mcmchstatus mcmchCloseIdStore(mcmchenv env) {
  _mcmchenv* _env = (_mcmchenv*)env;
  int idstorefd = _env->idstorefd;
  if (idstorefd == -1)
    return MCMCHSTATUS_SPECIFIC;
  mcmchlog(env, MCMCHLOG_INFO, 0, "Closing IDSTORE file.");
  fsync(idstorefd);
  close(idstorefd);
  _env->idstorefd = -1;
  return MCMCHSTATUS_SUCCESS;
}
#endif