package org.morkato.mcisid;

public class McisidNative {
  static {
    Mcisid.ensureLoadLib();
  }

  public static native byte getIdentifier(byte idx);
  public static native byte getVersionStrategy(String input);
  public static native byte getLookup(char c);
}
