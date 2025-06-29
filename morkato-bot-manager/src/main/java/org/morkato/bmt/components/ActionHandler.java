package org.morkato.bmt.components;

import org.morkato.bmt.actions.ActionSession;
import org.morkato.bmt.actions.ActionContext;
import org.morkato.bmt.actions.LayoutAction;

public interface ActionHandler<T> {
  void registerAction(LayoutAction action);
  void handleAction(ActionContext<T> ctx);
  default void onSessionExpired(ActionSession<T> session) {}
}
