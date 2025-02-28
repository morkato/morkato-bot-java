package org.morkato.bmt;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.management.CommandExceptionManager;
import org.morkato.bmt.management.CommandManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ComponentLoaderFactory {
  private final Map<Class<?>, Consumer<Object>> factories = new HashMap<>();
  private final CommandManager commands;
  private final CommandExceptionManager exceptions;
  public ComponentLoaderFactory(
    CommandManager commands,
    CommandExceptionManager exceptions
  ) {
    this.factories.put(Command.class, this::registryCommand);
    this.factories.put(CommandException.class, this::registryException);
    this.commands = commands;
    this.exceptions = exceptions;
  }
  public void registryCommand(Object object) {
    commands.register((Command<?>)object);
  }
  public void registryException(Object object) {
    exceptions.register((CommandException<?>)object);
  }
  public Consumer<Object> create(Class<?> clazz) {
    return factories.get(clazz);
  }
}
