package org.morkato.bmt.startup.management;

import org.morkato.bmt.generated.ExceptionsHandleStaticRegistries;
import org.morkato.bmt.generated.registries.CommandExceptionRegistry;
import org.morkato.boot.DependenceInjection;
import org.morkato.bmt.components.CommandExceptionHandler;
import org.morkato.boot.registration.RegistrationFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map;

public class StartupExceptionHandlerManagement
  extends StartupManagement
  implements RegistrationFactory<ExceptionsHandleStaticRegistries> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartupExceptionHandlerManagement.class);
  private final Map<Class<?>, CommandExceptionHandler<?>> commandExceptions = new HashMap<>();

  public StartupExceptionHandlerManagement(DependenceInjection injector) {
    super(injector);
  }

  public void registerCommandExceptionHandler(Class<?> clazz, CommandExceptionHandler<?> handler) {
    try {
      if (Objects.nonNull(commandExceptions.get(clazz))) {
        LOGGER.warn("Ignoring register for command exception handler: {}. This handler is already registered.", clazz.getName());
        return;
      }
      this.writeInRegistry(handler);
      commandExceptions.put(clazz, handler);
      LOGGER.debug("Command Exception Handler: {} has been registered for bootstrap.", handler);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then register command exception handler: {}.", handler, exc);
    }
  }

  @SuppressWarnings("unchecked")
  private <E extends Exception> CommandExceptionRegistry<E> flushCommandExceptionRegistry(
    Class<?> clazz,
    CommandExceptionHandler<?> handler
  ) {
    return new CommandExceptionRegistry<>((CommandExceptionHandler<E>)handler, (Class<E>)clazz);
  }

  @Override
  public ExceptionsHandleStaticRegistries flush() {
    final Map<Class<?>, CommandExceptionRegistry<?>> registries = new HashMap<>();
    for (Map.Entry<Class<?>, CommandExceptionHandler<?>> entry : commandExceptions.entrySet()) {
      Class<?> clazz = entry.getKey();
      CommandExceptionHandler<?> handler = entry.getValue();
      registries.put(clazz, this.flushCommandExceptionRegistry(clazz, handler));
      LOGGER.info("Success to flush command exception handler: {} ({}). The content is available for requests.", handler, clazz);
    }
    return new ExceptionsHandleStaticRegistries(registries);
  }
}
