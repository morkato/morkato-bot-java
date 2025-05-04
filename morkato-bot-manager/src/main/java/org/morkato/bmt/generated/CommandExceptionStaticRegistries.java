package org.morkato.bmt.generated;

import org.morkato.bmt.generated.registries.CommandExceptionRegistry;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.components.CommandException;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class CommandExceptionStaticRegistries implements MapRegistryManagement<Class<? extends Throwable>, CommandException<?>> {
  private final Map<Class<? extends Throwable>, CommandException<?>> registries = new HashMap<>();

  public CommandExceptionStaticRegistries(ApplicationStaticRegistries registries) {
    for (CommandExceptionRegistry<?> registry : registries.getRegisteredExceptions()) {
      this.registries.put(registry.getExceptionClass(), registry.getRegistry());
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
