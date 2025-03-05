package org.morkato.bmt.management;

import org.morkato.bmt.argument.NoArgs;
import org.morkato.bmt.commands.CommandRegistry;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.errors.CommandAlreadyRegisteredException;
import org.morkato.bmt.errors.CommandArgumentParserNotFound;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandManager implements RegisterManagement<Command<?>> {
  private static Logger logger = LoggerFactory.getLogger(CommandManager.class);
  private final Map<Class<? extends Command>, CommandRegistry<?>> commands = new HashMap<>();
  private final Map<String, Class<? extends Command<?>>> names = new HashMap<>();
  private final CommandExceptionManager exceptions;
  private final ArgumentManager arguments;

  public CommandManager(@Nonnull CommandExceptionManager exceptions, @Nonnull ArgumentManager arguments) {
    Objects.requireNonNull(arguments);
    Objects.requireNonNull(exceptions);
    this.arguments = arguments;
    this.exceptions = exceptions;
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
    Class<? extends Command<?>> clazz = this.getNameValue(name);
    if (clazz == null)
      return null;
    return this.getRegistry(clazz);
  }
  @Override
  public void register(Command<?> command) {
    try {
      this.registerWithErrors(command);
    } catch (CommandAlreadyRegisteredException exc) {
      logger.warn("Command ID: {} is already registered. Ignoring.", exc.getCommandClassName());
    } catch (CommandArgumentParserNotFound exc) {
      logger.error("Failed to load Command ID: {}. Parser for: {} is not registered. Ignoring.", command.getClass().getName(), exc.getParserClassName());
    } catch (Throwable exc) {
      logger.error("Failed to load Command ID: {}. An unexpected error occurred: {}. Ignoring.", command.getClass().getName(), exc.getClass().getName(), exc);
    }
  }
  @SuppressWarnings("unchecked")
  public <T> CommandRegistry<T> registerWithErrors(Command<T> command)
    throws CommandArgumentParserNotFound,
           CommandAlreadyRegisteredException {
    Class<?> commandClass = command.getClass();
    CommandRegistry<?> other = commands.get(commandClass);
    if (Objects.nonNull(other))
      throw new CommandAlreadyRegisteredException(other);
    Class<T> argumentClazz = (Class<T>)Command.getArgument(commandClass.asSubclass(Command.class));
    ObjectParser<T> parser = argumentClazz != NoArgs.class ? arguments.getObjectParser(argumentClazz) : null;
    if (Objects.isNull(parser) && argumentClazz != NoArgs.class)
      throw new CommandArgumentParserNotFound(argumentClazz);
    CommandRegistry<T> registry = new CommandRegistry<>(exceptions, command, parser);
    commands.put(command.getClass(), registry);
    logger.info("Command ID: {} is registered.", registry.getCommandClassName());
    return registry;
  }

  public void setCommandName(
    @Nonnull Class<? extends Command<?>> command,
    @Nonnull String name
  ) {
    this.names.put(name, command);
    logger.info("Name pointer registered: {} into the command: {}.", name, command.getName());
  }
}
