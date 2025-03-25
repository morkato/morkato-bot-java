package org.morkato.bmt.registration.hooks;

import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.NoArgs;
import org.morkato.bmt.exception.CommandAlreadyRegisteredException;
import org.morkato.bmt.exception.CommandArgumentParserNotFound;
import org.morkato.bmt.registration.registries.CommandRegistry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;

public class TextCommandRegistration
  extends ObjectRegistration<CommandRegistry<?>, Command<?>>
  implements MapRegistryManagement<Class<? extends Command<?>>, CommandRegistry<?>> {

  private final Logger LOGGER = LoggerFactory.getLogger(TextCommandRegistration.class);
  private final MapRegistryManagement<Class<?>, ObjectParser<?>> arguments;

  public TextCommandRegistration(
    MapRegistryManagement<Class<?>, ObjectParser<?>> arguments
  ) {
    Objects.requireNonNull(arguments);
    this.arguments = arguments;
  }

  @SuppressWarnings("unchecked")
  public <T> CommandRegistry<T> registerWithErrors(Command<T> command)
    throws CommandArgumentParserNotFound,
           CommandAlreadyRegisteredException {
    Class<? extends Command> commandClass = command.getClass();
    /* TODO: Add command already exists error */
    Class<T> argumentClazz = (Class<T>)Command.getArgument(commandClass.asSubclass(Command.class));
    ObjectParser<T> parser = argumentClazz != NoArgs.class ? (ObjectParser<T>)arguments.get(argumentClazz) : null;
    if (Objects.isNull(parser) && argumentClazz != NoArgs.class)
      throw new CommandArgumentParserNotFound(argumentClazz);
    CommandRegistry<T> registry = new CommandRegistry<>(command, parser, argumentClazz);
    this.add(commandClass, registry);
    return registry;
  }

  @Override
  public CommandRegistry<?> get(Class<? extends Command<?>> key) {
    return this.getRegistry(key);
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
  public void flush() {
    for (CommandRegistry<?> registry : this.registries()) {
      final Class<? extends Command<?>> childrenClazzCommand = registry.getCommandClass();
      final Class<? extends Command<?>> dadClazzCommand = registry.getParentClass();
      if (Objects.isNull(dadClazzCommand))
        continue;
      final CommandRegistry<?> children = this.get(childrenClazzCommand);
      final CommandRegistry<?> dad = this.get(dadClazzCommand);
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
}
