package org.morkato.mcisid;

import java.nio.charset.StandardCharsets;

public class Mcisid {
  static {
    System.loadLibrary("jnimcisid");
  }

  public static void ensureLoadLib() {}

  public static byte getIdentifier(byte identifier) {
    return McisidNative.getIdentifier((byte)(identifier & 0x3F));
  }

  public static byte getLookup(char lookup) {
    return McisidNative.getLookup(lookup);
  }

  public static String packInIdentifiers(byte[] lookups, int size) {
    final byte[] identifiers = new byte[size];
    for (int i = 0; i < size; ++i)
      identifiers[i] = getIdentifier(lookups[i]);
    return new String(identifiers, StandardCharsets.US_ASCII);
  }

  public static byte[] unpackInIdentifiers(String lookups, int size) {
    final byte[] identifiers = new byte[size];
    for (int i = 0; i < size; ++i)
      identifiers[i] = getLookup((char)(lookups.charAt(i) & 0xFF));
    return identifiers;
  }
}
