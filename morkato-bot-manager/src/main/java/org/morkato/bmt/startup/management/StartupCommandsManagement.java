package org.morkato.bmt.startup.management;

import org.morkato.bmt.startup.attributes.SlashCommandAttributes;
import org.morkato.bmt.startup.attributes.CommandAttributes;
import org.morkato.bmt.startup.payload.SlashCommandPayload;
import org.morkato.bmt.startup.payload.CommandPayload;
import org.morkato.bmt.generated.registries.ObjectParserRegistry;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;
import org.morkato.bmt.generated.registries.SlashMapperRegistry;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.generated.CommandsStaticRegistries;
import org.morkato.bmt.generated.ParsersStaticRegistries;
import org.morkato.bmt.exception.ArgumentParserException;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import org.morkato.bmt.components.CommandHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class StartupCommandsManagement
  extends StartupManagement
  implements RegistrationFactory<CommandsStaticRegistries> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartupCommandsManagement.class);
  private final Set<CommandPayload<?>> textcommands = new HashSet<>();
  private final Set<SlashCommandPayload<?>> slashcommands = new HashSet<>();
  private final Set<String> usedTextNames = new HashSet<>();
  private final Set<String> usedSlashNames = new HashSet<>();
  private ParsersStaticRegistries parsers;

  public StartupCommandsManagement(DependenceInjection injector) {
    super(injector);
  }

  private boolean textCommandAlreadyExists(CommandAttributes attributes) {
    if (usedTextNames.contains(attributes.getName()))
      return true;
    for (String alias : attributes.getAliases()) {
      if (usedTextNames.contains(alias))
        return true;
    }
    return false;
  }

  private boolean slashCommandAlreadyExists(SlashCommandAttributes attributes) {
    return usedSlashNames.contains(attributes.getName());
  }

  public <T> void registerTextCommand(CommandPayload<T> payload) {
    try {
      final CommandAttributes attributes = payload.attrs();
      final CommandHandler<T> command = payload.command();
      if (this.textCommandAlreadyExists(attributes)) {
        LOGGER.warn("");
        return;
      }
      this.writeInRegistry(command);
      textcommands.add(payload);
      usedTextNames.add(attributes.getName());
      usedTextNames.addAll(Arrays.asList(attributes.getAliases()));
      LOGGER.debug("Command: {} ({}) has been registered for bootstrap.", command, attributes.getName());
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then register command: {}.", payload.command(), exc);
    }
  }

  public <T> void registerSlashCommand(SlashCommandPayload<T> payload) {
    try {
      final SlashCommandAttributes attributes = payload.attrs();
      final CommandHandler<T> slashcommand = payload.slashcommand();
      if (this.slashCommandAlreadyExists(attributes)) {
        LOGGER.warn("");
        return;
      }
      this.writeInRegistry(slashcommand);
      slashcommands.add(payload);
      usedTextNames.add(attributes.getName());
      LOGGER.debug("Slash Command: {} ({}) has been registered for bootstrap.", slashcommand, attributes.getName());
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then register slash command: {}.", payload.slashcommand(), exc);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> CommandRegistry<T> flushTextCommand(CommandAttributes attributes, CommandHandler<T> command)
    throws ArgumentParserException {
    ObjectParserRegistry<T> parser = (ObjectParserRegistry<T>)parsers.getObjectParser(attributes.getArgumentClass());
    if (Objects.isNull(parser))
      throw new ArgumentParserException("");
    return new CommandRegistry<>(attributes, command, parser);
  }

  private Set<CommandRegistry<?>> flushTextCommands() {
    final Set<CommandRegistry<?>> registries = new HashSet<>();
    for (CommandPayload<?> payload : textcommands) {
      final CommandAttributes attributes = payload.attrs();
      final CommandHandler<?> command = payload.command();
      try {
        registries.add(this.flushTextCommand(attributes, command));
        LOGGER.info("Success to flush command: {} ({}). The content is available for requests.", command, attributes.getName());
      } catch (Exception exc) {
        LOGGER.error("An unexpected error occurred while flushing command: {} ({}).", command, attributes.getName(), exc);
      }
    }
    return registries;
  }

  @SuppressWarnings("unchecked")
  private <T> SlashCommandRegistry<T> flushSlashCommand(SlashCommandAttributes attributes, CommandHandler<T> slashcommand)
    throws ArgumentParserException {
    SlashMapperRegistry<T> mapper = (SlashMapperRegistry<T>)parsers.getSlashMapper(attributes.getArgumentClass());
    if (Objects.isNull(mapper))
      throw new ArgumentParserException("");
    return new SlashCommandRegistry<>(slashcommand, mapper, attributes);
  }

  private Set<SlashCommandRegistry<?>> flushSlashCommands() {
    final Set<SlashCommandRegistry<?>> registries = new HashSet<>();
    for (SlashCommandPayload<?> payload : slashcommands) {
      final SlashCommandAttributes attributes = payload.attrs();
      final CommandHandler<?> slashcommand = payload.slashcommand();
      try {
        registries.add(this.flushSlashCommand(attributes, slashcommand));
        LOGGER.info("Success to flush slash command: {} ({}). The content is available for requests.", slashcommand, attributes.getName());
      } catch (Exception exc) {
        LOGGER.error("An unexpected error occurred while flushing command: {} ({}).", slashcommand, attributes.getName(), exc);
      }
    }
    return registries;
  }

  public void prepare(ParsersStaticRegistries staticParsers) {
    this.parsers = staticParsers;
  }

  @Override
  public CommandsStaticRegistries flush() {
    LOGGER.info("Flushing all commands and slash commands to generate static content.");
    final Set<CommandRegistry<?>> textcommands = this.flushTextCommands();
    final Set<SlashCommandRegistry<?>> slashcommands = this.flushSlashCommands();
    LOGGER.info("All commands and slash commands has already flushed!");
    return new CommandsStaticRegistries(textcommands, slashcommands);
  }
}
