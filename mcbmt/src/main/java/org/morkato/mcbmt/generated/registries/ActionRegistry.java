package org.morkato.mcbmt.generated.registries;

import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.startup.attributes.ActionAttributes;

public class ActionRegistry<T> {
  private final String customid;
  private final ActionHandler<T> actionhandler;
  public ActionRegistry(ActionAttributes attributes, ActionHandler<T> actionhandler) {
    this.customid = attributes.getName();
    this.actionhandler = actionhandler;
  }

  public String getCustomId() {
    return customid;
  }

  public ActionHandler<T> getActionHandler() {
    return actionhandler;
  }
}
