package org.morkato.bmt.startup.management;

import org.morkato.bmt.components.ActionHandler;
import org.morkato.bmt.generated.ActionsStaticRegistries;
import org.morkato.bmt.generated.registries.ActionRegistry;
import org.morkato.bmt.startup.attributes.ActionAttributes;
import org.morkato.bmt.startup.payload.ActionPayload;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class StartupActionsManagement
  extends StartupManagement
  implements RegistrationFactory<ActionsStaticRegistries> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartupActionsManagement.class);
  private final Set<ActionPayload<?>> actions = new HashSet<>();
  private final Set<String> usedActionNames = new HashSet<>();
  private final Set<Class<?>> usedActionClasses = new HashSet<>();

  public StartupActionsManagement(DependenceInjection injector) {
    super(injector);
  }

  private boolean actionAlreadyExists(ActionHandler<?> actionhandler, ActionAttributes attributes) {
    return usedActionClasses.contains(actionhandler.getClass()) || usedActionNames.contains(attributes.getName());
  }

  public <T> void registerAction(ActionPayload<T> actionpayload) {
    try {
      final ActionAttributes attributes = actionpayload.attrs();
      final ActionHandler<T> actionhandler = actionpayload.actionhandler();
      if (this.actionAlreadyExists(actionhandler, attributes)) {
        LOGGER.warn("Action '{}' or class '{}' already registered. Skipping registration.",
          attributes.getName(), actionhandler.getClass().getSimpleName());
        return;
      }
      this.writeInRegistry(actionhandler);
      usedActionNames.add(attributes.getName());
      usedActionClasses.add(actionhandler.getClass());
      actions.add(actionpayload);
      LOGGER.debug("Action: {} ({}) has been registered for bootstrap.", actionhandler, attributes.getName());
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then register action: {}.", actionpayload.actionhandler(), exc);
    }
  }

  @Override
  public ActionsStaticRegistries flush() {
    final Set<ActionRegistry<?>> registries = new HashSet<>();
    for (ActionPayload<?> payload : actions) {
      final ActionAttributes attributes = payload.attrs();
      final ActionHandler<?> actionhandler = payload.actionhandler();
      try {
        registries.add(new ActionRegistry<>(attributes, actionhandler));
        LOGGER.info("Success to flush action: {} ({}). The content is available for requests.", actionhandler, attributes.getName());
      } catch (Exception exc) {
        LOGGER.error("An unexpected error occurred while flushing action: {} ({}).", actionhandler, attributes.getName(), exc);
      }
    }
    return new ActionsStaticRegistries(registries);
  }
}
