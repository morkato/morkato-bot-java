package org.morkato.bmt.registration.management;

import org.morkato.bmt.NoArgs;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.exception.CommandArgumentParserNotFound;
import org.morkato.bmt.generated.registries.SlashMapperRegistry;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;
import org.morkato.bmt.registration.*;
import org.morkato.bmt.registration.payload.SlashCommandPayload;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Objects;

public class SlashCommandRegistryManagement
  extends SetObjectRegisterInfo<SlashCommandRegistry<?>>
  implements RegisterManagement<SlashCommandPayload<?>, SlashCommandRegistry<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SlashCommandRegistryManagement.class);
  private final MapRegistryManagement<Class<?>,SlashMapperRegistry<?>> mappers;

  public SlashCommandRegistryManagement(MapRegistryManagement<Class<?>,SlashMapperRegistry<?>> mappers) {
    this.mappers = Objects.requireNonNull(mappers);
  }

  @SuppressWarnings("unchecked")
  private <T> SlashCommandRegistry<T> registerWithExceptions(SlashCommandPayload<T> payload)
    throws CommandArgumentParserNotFound{
    CommandHandler<T> command = payload.slashcommand();
    Class<?> args = CommandHandler.getArgument(command.getClass());
    SlashMapperRegistry<T> mapper = (SlashMapperRegistry<T>)mappers.get(args);
    if (Objects.isNull(mapper) && args != NoArgs.class)
      throw new CommandArgumentParserNotFound(args);
    SlashCommandRegistry<T> registry = new SlashCommandRegistry<>(command, mapper, payload.attrs());
    this.add(registry);
    return registry;
  }

  @Override
  public void register(SlashCommandPayload<?> payload) {
    try {
      SlashCommandRegistry<?> registry = this.registerWithExceptions(payload);
      LOGGER.info("Slash Command with name: {} has been registered.", registry.getName());
    } catch (CommandArgumentParserNotFound exc) {
      LOGGER.warn("Failed to register command: {}. Object parser: {} is not found.", payload.slashcommand().getClass().getName(), exc.getParserClassName());
    } catch (Throwable exc) {
      LOGGER.error("Failed to register command: {}. An unexpected error occurred: {}.", payload.slashcommand().getClass().getName(), exc.getClass().getName(), exc);
    }
  }
}
