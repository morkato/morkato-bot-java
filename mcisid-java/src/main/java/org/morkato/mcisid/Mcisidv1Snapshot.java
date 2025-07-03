package org.morkato.mcisid;

public class Mcisidv1Snapshot {
  public final long instant;
  public final int seqnext;
  public final byte model;

  public static Mcisidv1Snapshot take(Mcisidv1Sequence seq, byte model) {
    return new Mcisidv1Snapshot(seq, model);
  }

  public Mcisidv1Snapshot(Mcisidv1Sequence seq, byte model) {
    this.instant = seq.lastime;
    this.seqnext = seq.current;
    this.model = model;
  }
}
