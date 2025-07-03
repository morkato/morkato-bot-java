package org.morkato.mcisid;

public class Mcisidv1Native {
  static {
    Mcisid.ensureLoadLib();
  }
  
  public static native int nextValue(Class<Mcisidv1Sequence> cls, Mcisidv1Sequence seq, long instantMillis);
  public static native void resetSequence(Class<Mcisidv1Sequence> cls, Mcisidv1Sequence seq);
  public static native boolean isValidId(byte[] raw);
  public static native byte getModelType(byte[] raw);
  public static native long getInstantCreated(byte[] raw);
  public static native int getSequenceValue(byte[] raw);
}
