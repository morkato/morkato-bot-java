package org.morkato.mcbmt.generated;

import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.generated.registries.ActionRegistry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ActionsStaticRegistries {
  private final Map<Class<?>, ActionRegistry<?>> mappedActions = new HashMap<>();
  private final ActionRegistry<?>[] actions;

  public ActionsStaticRegistries(Set<ActionRegistry<?>> registries) {
    this.actions = new ActionRegistry[registries.size()];
    Iterator<ActionRegistry<?>> actionsIterator = registries.iterator();
    for (int i = 0; i < this.actions.length; ++i) {
      final ActionRegistry<?> action = actionsIterator.next();
      this.mappedActions.put(action.getActionHandler().getClass(), action);
      this.actions[i] = action;
    }
  }

  public ActionRegistry<?> getAction(Class<?> clazz) {
    return mappedActions.get(clazz);
  }

  public ActionRegistry<?>[] getActions() {
    return actions;
  }

  public ActionHandler<?> getActionById(String actionid) {
    return null;
  }

  public int getRegisteredActionsLength() {
    return actions.length;
  }
}
