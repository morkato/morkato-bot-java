package org.morkato.mcisid;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class Mcisidv1 {
  public static final int MCISIDV1_MAX_MODELS = 0x40;
  public static final int MCISIDIO_SIZE = 12;
  public static final int MCISIDV1_SUCCESS = 0;
  public static final int MCISIDV1_OVERFLOW = 1;
  public static final int MCISIDV1_RESET = 2;
  public static final int MCISIDV1_INSTANT_CORRUPTED = 4;
  public static final int MCISIDV1_STUPID_IMPOSSIBLE_SCENARY = 5;
  private final byte[] raw = new byte[MCISIDIO_SIZE];

  public static Mcisidv1 fromString(String text) {
    return new Mcisidv1(text.getBytes(StandardCharsets.US_ASCII));
  }

  public static Mcisidv1 fromSnapshot(Mcisidv1Snapshot snapshot) {
    Objects.requireNonNull(snapshot);
    final byte[] raw = new byte[MCISIDIO_SIZE];
    /* HEADERS: [IDENTIFIER: 0][MODEL: 1 / 0x40] */
    raw[0] = Mcisid.getIdentifier((byte)0);
    raw[1] = Mcisid.getIdentifier(snapshot.model);
    /* SEQUENCE: 18bits [2 / 0x3F][3 / 0x3F][4 / 0x3F] */
    raw[2] = Mcisid.getIdentifier((byte)(snapshot.seqnext & 0x3F));
    raw[3] = Mcisid.getIdentifier((byte)((snapshot.seqnext >> 6) & 0x3F));
    raw[4] = Mcisid.getIdentifier((byte)((snapshot.seqnext >> 12) & 0x3F));
    /* TIMESTAMP: 42bits [5 / 0x3F][6 / 0x3F][7 / 0x3F][8 / 0x3F][9 / 0x3F][10 / 0x3F][11 / 0x3F] */
    raw[5] = Mcisid.getIdentifier((byte)(snapshot.instant & 0x3F));
    raw[6] = Mcisid.getIdentifier((byte)((snapshot.instant >> 6) & 0x3F));
    raw[7] = Mcisid.getIdentifier((byte)((snapshot.instant >> 12) & 0x3F));
    raw[8] = Mcisid.getIdentifier((byte)((snapshot.instant >> 18) & 0x3F));
    raw[9] = Mcisid.getIdentifier((byte)((snapshot.instant >> 24) & 0x3F));
    raw[10] = Mcisid.getIdentifier((byte)((snapshot.instant >> 30) & 0x3F));
    raw[11] = Mcisid.getIdentifier((byte)((snapshot.instant >> 36) & 0x3F));
    return new Mcisidv1(raw);
  }

  public Mcisidv1(byte[] raw) {
    if (raw.length != MCISIDIO_SIZE || !Mcisidv1Native.isValidId(raw))
      throw new IllegalArgumentException("An error");
    System.arraycopy(raw, 0, this.raw, 0, MCISIDIO_SIZE);
  }

  @Override
  public String toString() {
    return new String(raw, StandardCharsets.US_ASCII);
  }

  public byte[] getRaw() {
    return Arrays.copyOf(raw, MCISIDIO_SIZE);
  }

  public byte getIdentifier() {
    return (byte)0; // SEMPRE 0 PARA VERSÃO v1;
  }

  public byte getOriginModel() {
    return Mcisidv1Native.getModelType(raw);
  }

  public int getSequenceValue() {
    return Mcisidv1Native.getSequenceValue(raw);
  }

  public long getInstantCreated() {
    return Mcisidv1Native.getInstantCreated(raw);
  }
}
