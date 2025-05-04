package org.morkato.bmt.registration.management;

import org.morkato.bmt.exception.CommandArgumentParserNotFound;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.generated.registries.ObjectParserRegistry;
import org.morkato.bmt.registration.*;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.NoArgs;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;

public class CommandRegistryManagement
  extends SetObjectRegisterInfo<CommandRegistry<?>>
  implements RegisterManagement<CommandPayload<?>, CommandRegistry<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegistryManagement.class);
  private final MapRegistryManagement<Class<?>,ObjectParserRegistry<?>> arguments;

  public CommandRegistryManagement(MapRegistryManagement<Class<?>, ObjectParserRegistry<?>> arguments) {
    this.arguments = Objects.requireNonNull(arguments);
  }

  @SuppressWarnings("unchecked")
  private <T> CommandRegistry<T> registerWithExceptions(CommandPayload<T> payload)
    throws CommandArgumentParserNotFound {
    CommandHandler<T> command = payload.command();
    Class<?> args = CommandHandler.getArgument(command.getClass());
    ObjectParserRegistry<T> parser = args != NoArgs.class ? (ObjectParserRegistry<T>)arguments.get(args) : null;
    if (Objects.isNull(parser) && args != NoArgs.class)
      throw new CommandArgumentParserNotFound(args);
    CommandRegistry<T> registry = new CommandRegistry<>(payload.attrs(), command, parser);
    this.add(registry);
    return registry;
  }

  @Override
  public void register(CommandPayload<?> payload) {
    try {
      CommandRegistry<?> registry = this.registerWithExceptions(payload);
      LOGGER.info("Command ID: {} ({}) has been registered.", registry.getCommandClassName(), registry.getName());
    } catch (CommandArgumentParserNotFound exc) {
      LOGGER.warn("Failed to register command: {}. Object parser: {} is not found.", payload.command().getClass().getName(), exc.getParserClassName());
    } catch (Throwable exc) {
      LOGGER.error("Failed to register command: {}. An unexpected error occurred: {}.", payload.command().getClass().getName(), exc.getClass().getName(), exc);
    }
  }
}
