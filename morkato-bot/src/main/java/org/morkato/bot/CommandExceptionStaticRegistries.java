package org.morkato.bot;

import org.morkato.bmt.ApplicationRegistries;
import org.morkato.bmt.CommandRegistry;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.registration.MapRegistryManagement;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandExceptionStaticRegistries implements MapRegistryManagement<Class<? extends Throwable>, CommandException<?>> {
  private final Map<Class<? extends Throwable>, CommandException<?>> registries = new HashMap<>();

  public CommandExceptionStaticRegistries(ApplicationRegistries registries) {
    for (CommandException<?> handler : registries.getRegisteredExceptions()) {
      this.registries.put(CommandException.getExceptionClass(handler), handler);
    }
  }

  @Override
  public CommandException<?> get(Class<? extends Throwable> key) {
    CommandException<?> exception = this.registries.get(key);
    if (Objects.nonNull(exception))
      return exception;
    for (Map.Entry<Class<? extends Throwable>, CommandException<?>> entry : registries.entrySet()) {
      Class<? extends Throwable> clazz = entry.getKey();
      CommandException<?> other = entry.getValue();
      if (clazz.isAssignableFrom(key))
        return other;
    }
    return null;
  }
}
