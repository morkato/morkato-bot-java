package org.morkato.bmt.actions;

import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import org.morkato.utility.mcisid.McisidUtil;

public interface LayoutAction {
  static final char SPECIAL_CHARACTER_ACTION_SESSION = '-';
  static final char SPECIAL_CHARACTER_ACTION = '&';
  static String getComponentIdWithSession(int slot, String id) {
    return String.valueOf(SPECIAL_CHARACTER_ACTION_SESSION)
      + McisidUtil.getCharFromValue(slot & 0x3f)
      + McisidUtil.getCharFromValue((slot >> 6) & 0x3f)
      + id;
  }

  ButtonAction getButtonReference(String id);
  void button(String id, ButtonAction button);
  LayoutComponent[] build();
}
