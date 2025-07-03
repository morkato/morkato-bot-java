package org.morkato.mcisid;

public class Mcisidv1Sequence {
  public int current;
  public long lastime;

  public Mcisidv1Sequence() {
    this.reset();
  }

  public void reset() {
    Mcisidv1Native.resetSequence(Mcisidv1Sequence.class, this);
  }

  public int next(long instantMilis) {
    return Mcisidv1Native.nextValue(Mcisidv1Sequence.class, this, instantMilis);
  }
}