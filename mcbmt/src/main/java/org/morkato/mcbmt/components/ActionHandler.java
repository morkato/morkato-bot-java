package org.morkato.mcbmt.components;

import org.morkato.mcbmt.actions.ActionSession;
import org.morkato.mcbmt.actions.ActionContext;
import org.morkato.mcbmt.actions.LayoutAction;

public interface ActionHandler<T> {
  void registerAction(LayoutAction action);
  void handleAction(ActionContext<T> ctx);
  default void onSessionExpired(ActionSession<T> session) {}
}
