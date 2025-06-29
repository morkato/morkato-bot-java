package org.morkato.bmt.generated.registries;

import org.morkato.bmt.components.ActionHandler;
import org.morkato.bmt.startup.attributes.ActionAttributes;

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
