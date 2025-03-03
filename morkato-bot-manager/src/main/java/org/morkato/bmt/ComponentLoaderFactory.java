package org.morkato.bmt;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.bmt.management.CommandExceptionManager;
import org.morkato.bmt.management.CommandManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ComponentLoaderFactory {
  private final Map<Class<?>, Consumer<Object>> factories = new HashMap<>();
  private final CommandManager commands;
  private final CommandExceptionManager exceptions;
  private final ArgumentManager arguments;
  public ComponentLoaderFactory(
    CommandManager commands,
    CommandExceptionManager exceptions,
    ArgumentManager arguments
  ) {
    this.factories.put(Command.class, this::registryCommand);
    this.factories.put(CommandException.class, this::registryException);
    this.factories.put(ObjectParser.class, this::registryArgument);
    this.commands = commands;
    this.exceptions = exceptions;
    this.arguments = arguments;
  }
  public void registryCommand(Object object) {
    commands.register((Command<?>)object);
  }
  public void registryException(Object object) {
    exceptions.register((CommandException<?>)object);
  }
  public void registryArgument(Object object) {
    arguments.registerObjectParser((ObjectParser<?>)object);
  }
  public Consumer<Object> create(Class<?> clazz) {
    return factories.get(clazz);
  }
}
