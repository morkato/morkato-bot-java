package org.morkato.mcisid;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mcisidv1Generator {
  private final Mcisidv1Sequence[] sequences;
  private final Lock[] sequenceLocks;
  private final long epochInSeconds;

  public Mcisidv1Generator(long epochInSeconds) {
    final Mcisidv1Sequence[] sequences = new Mcisidv1Sequence[Mcisidv1.MCISIDV1_MAX_MODELS];
    final Lock[] locks = new Lock[sequences.length];
    for (int i = 0; i < sequences.length; ++i) {
      sequences[i] = new Mcisidv1Sequence();
      locks[i] = new ReentrantLock();
    }
    this.sequences = sequences;
    this.sequenceLocks = locks;
    this.epochInSeconds = epochInSeconds;
  }

  private long currentTime() {
    return System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(epochInSeconds);
  }

  private Lock getSequenceModelLock(byte model) {
    return sequenceLocks[model];
  }

  public Mcisidv1 generateId(byte model) {
    Lock lock = this.getSequenceModelLock(model);
    int status = 33; // APIINTERNA C, DPS IREI ABSTRAIR O CÓDIGO
    Mcisidv1Sequence sequence = sequences[model];
    Mcisidv1Snapshot snapshot = null;
    long instantInMillis = this.currentTime();
    lock.lock();
    try {
      status = sequence.next(instantInMillis);
      for (byte tries = 0; tries < 10; ++tries) {
        if (status == Mcisidv1.MCISIDV1_SUCCESS)
          break;
        else if (status == Mcisidv1.MCISIDV1_OVERFLOW)
          Thread.sleep(1);
        else if (status == Mcisidv1.MCISIDV1_RESET)
          sequence.reset();
        else if (status == Mcisidv1.MCISIDV1_INSTANT_CORRUPTED)
          instantInMillis = this.currentTime();
        else break;
        status = sequence.next(instantInMillis);
      }
      if (status != Mcisidv1.MCISIDV1_SUCCESS)
        /* TODO: Adicionar um erro mais bacana aqui. */
        throw new RuntimeException("An error occurred! Error status: " + status);
      snapshot = Mcisidv1Snapshot.take(sequence, model);
    } catch (InterruptedException exc) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
    return Mcisidv1.fromSnapshot(snapshot);
  }
}