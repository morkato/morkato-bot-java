package org.morkato.bmt.generated;

import org.morkato.bmt.generated.registries.CommandExceptionRegistry;

import java.util.Iterator;
import java.util.Map;

public class ExceptionsHandleStaticRegistries {
  private final CommandExceptionRegistry[] commandExceptions;
  private final Map<Class<?>, CommandExceptionRegistry> mapCommandExceptions;
  public ExceptionsHandleStaticRegistries(Map<Class<?>, CommandExceptionRegistry> registries) {
    this.commandExceptions = new CommandExceptionRegistry[registries.size()];
    this.mapCommandExceptions = registries;
    Iterator<CommandExceptionRegistry> commandExceptionsIterator = registries.values().iterator();
    for (int i = 0; i < this.commandExceptions.length; ++i) {
      this.commandExceptions[i] = commandExceptionsIterator.next();
    }
  }

  public CommandExceptionRegistry getCommandExceptionHandler(Class<?> clazz) {
    return this.mapCommandExceptions.get(clazz);
  }
}
