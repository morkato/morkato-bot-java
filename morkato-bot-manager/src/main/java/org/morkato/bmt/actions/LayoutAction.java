package org.morkato.bmt.actions;

import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import org.morkato.mcisid.Mcisid;

public interface LayoutAction {
  static final char SPECIAL_CHARACTER_ACTION_SESSION = '-';
  static final char SPECIAL_CHARACTER_ACTION = '&';
  static String getComponentIdWithSession(int slot, String id) {
    byte[] packedSlots = new byte[] { (byte)(slot & 0x3F), (byte)((slot >> 6) & 0x3F) };
    String identifiers = Mcisid.packInIdentifiers(packedSlots, 2);
    return String.valueOf(SPECIAL_CHARACTER_ACTION_SESSION)
      + identifiers
      + id;
  }

  static int getUnpackedSlots(String lookups) {
    byte[] identifiers = Mcisid.unpackInIdentifiers(lookups, 2);
    int i0 = (int)identifiers[0];
    int i1 = (int)identifiers[1];
    return (int)((i1 << 6) | i0);
  }

  ButtonAction getButtonReference(String id);
  void button(String id, ButtonAction button);
  LayoutComponent[] build();
}
