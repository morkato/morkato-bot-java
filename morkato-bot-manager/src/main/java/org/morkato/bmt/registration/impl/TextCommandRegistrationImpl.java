package org.morkato.bmt.registration.impl;

import org.morkato.bmt.SubCommandPayload;
import org.morkato.bmt.registration.TextCommandExceptionRegistration;
import org.morkato.bmt.registration.TextCommandRegistration;
import org.morkato.bmt.registration.ArgumentRegistration;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.NoArgs;
import org.morkato.bmt.exception.CommandAlreadyRegisteredException;
import org.morkato.bmt.exception.CommandArgumentParserNotFound;
import org.morkato.bmt.CommandRegistry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.*;

public class TextCommandRegistrationImpl implements TextCommandRegistration {
  private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandRegistration.class);
  private final Map<Class<? extends Command>, CommandRegistry<?>> registries = new HashMap<>();
  private final Set<SubCommandPayload> subcommands = new HashSet<>();
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
  public void registerSubCommand(SubCommandPayload payload) {
    Objects.requireNonNull(payload.dad());
    Objects.requireNonNull(payload.children());
    Objects.requireNonNull(payload.pointer());
    subcommands.add(payload);
  }

  @Override
  public void flush() {
    for (CommandRegistry<?> registry : registries.values()) {
      final Class<? extends Command<?>> childrenClazzCommand = registry.getCommandClass();
      final Class<? extends Command<?>> dadClazzCommand = registry.getParentClass();
      if (Objects.isNull(dadClazzCommand))
        continue;
      final CommandRegistry<?> children = this.getRegistry(childrenClazzCommand);
      final CommandRegistry<?> dad = this.getRegistry(dadClazzCommand);
      if (Objects.isNull(children)) {
        LOGGER.warn("Children command: {} is not registered to pointer for dad command: {}. Ignoring.", childrenClazzCommand.getName(), dadClazzCommand.getName());
        continue;
      }
      if (Objects.isNull(dad)) {
        LOGGER.warn("Dad command: {} is not registered to pointer for children command: {}. Ignoring.", dadClazzCommand.getName(), childrenClazzCommand.getName());
        continue;
      }
      dad.registerSubCommand(registry.getChildrenName(), children);
      LOGGER.info("Registered a children command: {} in dad command: {}.", childrenClazzCommand.getName(), dadClazzCommand.getName());
    }
  }

  @Override
  public void clear() {
    registries.clear();
    subcommands.clear();
  }
}
