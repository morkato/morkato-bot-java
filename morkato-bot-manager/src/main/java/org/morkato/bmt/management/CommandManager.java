package org.morkato.bmt.management;

import org.morkato.bmt.commands.CommandRegistry;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.Extension;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandManager {
  private static Logger logger = LoggerFactory.getLogger(CommandManager.class);
  private final Map<Class<? extends Command>, CommandRegistry<?>> commands = new HashMap<>();
  private Map<String, Class<? extends Command<?>>> names = new HashMap<>();
  private CommandExceptionManager exceptions = new CommandExceptionManager();

  @Nonnull
  public CommandExceptionManager getExceptionManager() {
    return this.exceptions;
  }

  public Class<? extends Command<?>> getNameValue(
    @Nonnull String name
  ) {
    return this.names.get(name);
  }

  public CommandRegistry<?> getRegistry(
    @Nonnull Class<? extends Command> clazz
  ) {
    return this.commands.get(clazz);
  }

  public Set<String> getAllNames(Class<? extends Command> commandClass){
    return this.names.entrySet()
      .stream()
      .filter((entry) -> entry.getValue().equals(commandClass))
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }

  public CommandRegistry<?> getRegistryByName(
    @Nonnull String name
  ) {
    Class<? extends Command> clazz = this.getNameValue(name);
    if (clazz == null)
      return null;
    return this.getRegistry(clazz);
  }
  @SuppressWarnings("unchecked")
  public <T> CommandRegistry<?> register(Command<T> command) {
    Class<?> commandClass = command.getClass();
    CommandRegistry<?> other = commands.get(commandClass);
    if (other != null) {
      logger.debug("Command ID: {} already registered. Ignoring...", other.getCommandClassName());
      return other;
    }
    CommandRegistry<T> registry = new CommandRegistry<>(exceptions, command);
    commands.put(command.getClass(), registry);
    logger.debug("Command ID: {} is registered.", registry.getCommandClassName());
    return registry;
  }

  public void setCommandName(
    @Nonnull Class<? extends Command<?>> command,
    @Nonnull String name
  ) {
    this.names.put(name, command);
    logger.debug("Name pointer registered: {} into the command: {}.", name, command.getName());
  }
}
