package org.morkato.bmt.bmt.registration.impl;

import org.morkato.bmt.bmt.registration.TextCommandExceptionRegistration;
import org.morkato.bmt.bmt.registration.TextCommandRegistration;
import org.morkato.bmt.bmt.registration.ArgumentRegistration;
import org.morkato.bmt.bmt.components.ObjectParser;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.argument.NoArgs;
import org.morkato.bmt.bmt.errors.CommandAlreadyRegisteredException;
import org.morkato.bmt.bmt.errors.CommandArgumentParserNotFound;
import org.morkato.bmt.bmt.commands.CommandRegistry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.*;

public class TextCommandRegistrationImpl implements TextCommandRegistration {
  private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandRegistration.class);
  private final Map<Class<? extends Command>, CommandRegistry<?>> registries = new HashMap<>();
  private final TextCommandExceptionRegistration exceptions;
  private final ArgumentRegistration arguments;
  public TextCommandRegistrationImpl(TextCommandExceptionRegistration exceptions, ArgumentRegistration arguments) {
    Objects.requireNonNull(exceptions);
    Objects.requireNonNull(arguments);
    this.exceptions = exceptions;
    this.arguments = arguments;
  }

  @Override
  public CommandRegistry<?> getRegistry(Class<? extends Command<?>> registry) {
    return registries.get(registry);
  }

  @SuppressWarnings("unchecked")
  public synchronized <T> CommandRegistry<T> registerWithErrors(Command<T> command)
    throws CommandArgumentParserNotFound,
           CommandAlreadyRegisteredException {
    Class<? extends Command> commandClass = command.getClass();
    CommandRegistry<?> other = registries.get(commandClass);
    if (Objects.nonNull(other))
      throw new CommandAlreadyRegisteredException(other);
    Class<T> argumentClazz = (Class<T>)Command.getArgument(commandClass.asSubclass(Command.class));
    ObjectParser<T> parser = argumentClazz != NoArgs.class ? arguments.getParser(argumentClazz) : null;
    if (Objects.isNull(parser) && argumentClazz != NoArgs.class)
      throw new CommandArgumentParserNotFound(argumentClazz);
    CommandRegistry<T> registry = new CommandRegistry<>(exceptions, command, parser);
    registries.put(commandClass, registry);
    return registry;
  }

  @Override
  public void register(Command<?> command) {
    try {
      CommandRegistry<?> registry = this.registerWithErrors(command);
      LOGGER.info("Command ID: {} has been registered.", registry.getCommandClassName());
    } catch (CommandArgumentParserNotFound exc) {
      LOGGER.warn("Failed to register command: {}. Object parser: {} is not found.", command.getClass().getName(), exc.getParserClassName());
    } catch (CommandAlreadyRegisteredException exc) {
      LOGGER.warn("Failed to register command: {}. Command already registered.", command.getClass().getName());
    } catch (Throwable exc) {
      LOGGER.error("Failed to register command: {}. An unexpected error occurred: {}.", command.getClass().getName(), exc.getClass().getName(), exc);
    }
  }

  @Override
  public void clear() {
    registries.clear();
  }
}
