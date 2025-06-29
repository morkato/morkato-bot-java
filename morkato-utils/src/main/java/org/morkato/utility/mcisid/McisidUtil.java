package org.morkato.utility.mcisid;

public class McisidUtil {
  private static final String IDENTIFIERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-";
  private static final int MCISIDIO_SIZE = 12;
  public static boolean isIdModel(String mcisid, int type) {
    int capturedModel = getV1OriginModel(mcisid);
    if (capturedModel == -1)
      return false;
    return capturedModel == type;
  }
  public static boolean isIdModel(String mcisid, ModelType type) {
    return isIdModel(mcisid, type.getRawValue());
  }
  public static boolean isValidId(String mcisid) {
    if (mcisid.length() != MCISIDIO_SIZE)
      return false;
    for (int i = 0; i < MCISIDIO_SIZE; ++i) {
      if (IDENTIFIERS.indexOf(mcisid.charAt(i)) == -1)
        return false;
    }
    return true;
  }
  public static int getIdentifier(String mcisid) {
    return IDENTIFIERS.indexOf(mcisid.charAt(0));
  }
  public static int getV1OriginModel(String mcisid) {
    if (mcisid.length() != MCISIDIO_SIZE)
      return -1;
    return IDENTIFIERS.indexOf(mcisid.charAt(1));
  }

  public static int getV1Sequence(String mcisid) {
    int seq0 = IDENTIFIERS.indexOf(mcisid.charAt(2));
    int seq1 = IDENTIFIERS.indexOf(mcisid.charAt(3));
    int seq2 = IDENTIFIERS.indexOf(mcisid.charAt(4));
    return (seq2 << 12) | (seq1 << 6) | seq0;
  }

  public static long getV1InstantInMilis(String mcisid) {
    long t0 = IDENTIFIERS.indexOf(mcisid.charAt(5));
    long t1 = IDENTIFIERS.indexOf(mcisid.charAt(6));
    long t2 = IDENTIFIERS.indexOf(mcisid.charAt(7));
    long t3 = IDENTIFIERS.indexOf(mcisid.charAt(8));
    long t4 = IDENTIFIERS.indexOf(mcisid.charAt(9));
    long t5 = IDENTIFIERS.indexOf(mcisid.charAt(10));
    long t6 = IDENTIFIERS.indexOf(mcisid.charAt(11));
    return (t6 << 36) | (t5 << 30) | (t4 << 24) | (t3 << 18) | (t2 << 12) | (t1 << 6) | t0;
  }

  public static int getIntValue(String sessionid) {
    if (sessionid.length() > 5)
      /* TODO: Add custom exception */
      throw new RuntimeException("Message");
    int value = 0;
    for (int i = 0; i < sessionid.length(); ++i)
      value |= (IDENTIFIERS.indexOf(sessionid.charAt(i)) << (6 * i));
    return value;
  }

  public static char getCharFromValue(int value) {
    return IDENTIFIERS.charAt(value & 0x3f);
  }
}
