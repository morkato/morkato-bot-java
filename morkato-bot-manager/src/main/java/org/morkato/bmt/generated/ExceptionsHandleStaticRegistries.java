package org.morkato.bmt.generated;

import org.morkato.bmt.generated.registries.CommandExceptionRegistry;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ExceptionsHandleStaticRegistries {
  private final CommandExceptionRegistry<?>[] commandExceptions;
  private final Map<Class<?>, CommandExceptionRegistry<?>> mapCommandExceptions;
  public ExceptionsHandleStaticRegistries(Map<Class<?>, CommandExceptionRegistry<?>> registries) {
    this.commandExceptions = new CommandExceptionRegistry[registries.size()];
    this.mapCommandExceptions = registries;
    Iterator<CommandExceptionRegistry<?>> commandExceptionsIterator = registries.values().iterator();
    for (int i = 0; i < this.commandExceptions.length; ++i) {
      this.commandExceptions[i] = commandExceptionsIterator.next();
    }
  }

  public CommandExceptionRegistry<?> getCommandExceptionHandler(Class<?> clazz) {
    CommandExceptionRegistry<?> registry = this.mapCommandExceptions.get(clazz);
    if (Objects.nonNull(registry))
      return registry;
    for (Map.Entry<Class<?>, CommandExceptionRegistry<?>> entry : mapCommandExceptions.entrySet()) {
      Class<?> supposedDadClass = entry.getKey();
      if (supposedDadClass.isAssignableFrom(clazz))
        return entry.getValue();
    }
    return null;
  }
}
