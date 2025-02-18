package com.morkato.bmt.commands;

import com.morkato.bmt.function.ExceptionFunction;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class CommandManager{
  private static Logger logger = LoggerFactory.getLogger(CommandManager.class);
  private Map<Class<?>, Command<?>> commands = new HashMap<>();
  private Map<String, Class<?>> names = new HashMap<>();
  private Map<Class<?>,ExceptionFunction<?>> exceptions;
  private String prefix;

  public CommandManager(
    Map<Class<?>,ExceptionFunction<?>> exceptions
  ) {
    this.exceptions = exceptions;
  }

  public String getPrefix() {
    return this.prefix;
  }
  public void setPrefix(
    @Nonnull String newvl
  ) {
    this.prefix = newvl;
  }

  @SuppressWarnings("unchecked")
  public <T extends Throwable> ExceptionFunction<T> getExceptionHandler(Class<T> clazz) {
    ExceptionFunction<?> handler = exceptions.get(clazz);
    if (handler != null)
      return (ExceptionFunction<T>) handler;
    Class<?> current = clazz.getSuperclass();
    while (current != null && Throwable.class.isAssignableFrom(current)) {
      handler = exceptions.get(current);
      if (handler != null) {
        return (ExceptionFunction<T>) handler;
      }
      current = current.getSuperclass();
    }
    return null;
  }

  @Nonnull
  public Map<Class<?>, Command<?>> getCommands() {
    return this.commands;
  }
  @Nonnull
  public Map<String, Class<?>> getNames() {
    return this.names;
  }

  public Class<?> getNameValue(
    @Nonnull String name
  ) {
    return this.names.get(name);
  }

  public Command<?> getCommandInstance(
    @Nonnull Class<?> clazz
  ) {
    return this.commands.get(clazz);
  }

  public Command<?> getCommandByName(
    @Nonnull String name
  ) {
    Class<?> clazz = this.getNameValue(name);
    if (clazz == null)
      return null;
    return this.getCommandInstance(clazz);
  }

  public void registry(Command<?> command) {
    Class<?> commandClass = command.getClass();
    Command other = commands.get(commandClass);
    if (other != null) {
      logger.debug("Command ID: {} already registered. Ignoring...", other.getClass().getName());
      return;
    }
    commands.put(commandClass, command);
    logger.debug("Command ID: {} is registered.", command.getClass().getName());
  }

  public void setCommandName(
    @Nonnull Class<?> command,
    @Nonnull String name
  ) {
    this.names.put(name, command);
  }
}
