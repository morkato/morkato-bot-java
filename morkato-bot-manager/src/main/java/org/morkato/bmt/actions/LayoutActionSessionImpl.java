package org.morkato.bmt.actions;

import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import java.util.*;

public class LayoutActionSessionImpl implements LayoutAction {
  private final Map<String, ButtonAction> buttons = new LinkedHashMap<>();
  private int sessionSlot;

  public void setSessionSlot(int slot) {
    this.sessionSlot = slot;
  }

  @Override
  public ButtonAction getButtonReference(String id){
    return Objects.requireNonNull(buttons.get(id));
  }

  @Override
  public void button(String id, ButtonAction button) {
    this.buttons.put(id, button);
  }

  @Override
  public LayoutComponent[] build() {
    final Set<ItemComponent> items = new LinkedHashSet<>();
    for (Map.Entry<String, ButtonAction> entry : buttons.entrySet()) {
      String customId = entry.getKey();
      ButtonAction button = entry.getValue();
      String componentid = LayoutAction.getComponentIdWithSession(sessionSlot, customId);
      Button jdaButton = Button.of(
        button.getStyle(),
        componentid,
        button.getLabel(),
        button.getEmoji()
      ).withDisabled(button.isDisabled());
      items.add(jdaButton);
    }
    return new LayoutComponent[] {
      ActionRow.of(items)
    };
  }
}
