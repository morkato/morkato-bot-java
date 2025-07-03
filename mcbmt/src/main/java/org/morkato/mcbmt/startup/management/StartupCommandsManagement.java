package org.morkato.mcbmt.startup.management;

import org.morkato.mcbmt.generated.ActionsStaticRegistries;
import org.morkato.mcbmt.generated.registries.CommandRegistry;
import org.morkato.mcbmt.generated.registries.ObjectParserRegistry;
import org.morkato.mcbmt.generated.registries.SlashCommandRegistry;
import org.morkato.mcbmt.generated.registries.SlashMapperRegistry;
import org.morkato.mcbmt.startup.CommandsInitialization;
import org.morkato.mcbmt.startup.attributes.SlashCommandAttributes;
import org.morkato.mcbmt.startup.attributes.CommandAttributes;
import org.morkato.mcbmt.startup.payload.SlashCommandPayload;
import org.morkato.mcbmt.startup.payload.CommandPayload;
import org.morkato.mcbmt.generated.CommandsStaticRegistries;
import org.morkato.mcbmt.generated.ParsersStaticRegistries;
import org.morkato.mcbmt.exception.ArgumentParserException;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import org.morkato.mcbmt.components.CommandHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;

public class StartupCommandsManagement
  extends StartupManagement
  implements RegistrationFactory<CommandsStaticRegistries> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartupCommandsManagement.class);
  private final Map<Class<?>, CommandHandler<?>> handlers = new HashMap<>();
  private final Set<CommandPayload<?>> textcommands = new HashSet<>();
  private final Set<SlashCommandPayload<?>> slashcommands = new HashSet<>();
  private final Set<String> usedTextNames = new HashSet<>();
  private final Set<String> usedSlashNames = new HashSet<>();
  private ActionsStaticRegistries actions;
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

  public <T extends CommandHandler<?>> void registerCommandHandler(T handler) {
    try {
      if (Objects.nonNull(handlers.get(handler.getClass()))) {
        LOGGER.warn("Command handler {} already registered. Skipping...", handler.getClass().getSimpleName());
        return;
      }
      this.writeInRegistry(handler);
      handlers.put(handler.getClass(), handler);
      LOGGER.debug("Command Handler: {} has been registered for bootstrap.", handler);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then register command handler: {}.", handler, exc);
    }
  }

  public <T> void registerTextCommand(CommandPayload<T> payload) {
    try {
      final CommandAttributes attributes = payload.attrs();
      final Class<? extends CommandHandler<T>> command = payload.command();
      if (this.textCommandAlreadyExists(attributes)) {
        LOGGER.warn("Text command name or alias '{}' is already in use. Skipping registration.", attributes.getName());
        return;
      }
      textcommands.add(payload);
      usedTextNames.add(attributes.getName());
      usedTextNames.addAll(Arrays.asList(attributes.getAliases()));
      LOGGER.debug("Command: {} ({}) has been registered for bootstrap", attributes.getName(), command.getName());
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then register command: {}.", payload.command(), exc);

    }
  }

  public <T> void registerSlashCommand(SlashCommandPayload<T> payload) {
    try {
      final SlashCommandAttributes attributes = payload.attrs();
      final Class<? extends CommandHandler<T>> slashcommand = payload.slashcommand();
      if (this.slashCommandAlreadyExists(attributes)) {
        LOGGER.warn("");
        return;
      }
      slashcommands.add(payload);
      usedSlashNames.add(attributes.getName());
      LOGGER.debug("Slash Command: {} ({}) has been registered for bootstrap", attributes.getName(), slashcommand.getName());
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then register slash command: {}.", payload.slashcommand(), exc);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> CommandRegistry<T> flushTextCommand(CommandAttributes attributes,CommandHandler<T> command)
    throws ArgumentParserException {
    ObjectParserRegistry<T> parser = (ObjectParserRegistry<T>)parsers.getObjectParser(attributes.getArgumentClass());
    if (Objects.isNull(parser))
      throw new ArgumentParserException("");
    return new CommandRegistry<>(attributes, command, parser);
  }

  private Map<Class<?>, CommandHandler<?>> flushCommandHandler(CommandsInitialization init) {
    final Map<Class<?>, CommandHandler<?>> registries = new HashMap<>();
    for (CommandHandler<?> handler : handlers.values()) {
      try {
        handler.initialize(init);
        registries.put(handler.getClass(), handler);
      } catch (Exception exc) {
        LOGGER.error("An unexpected error occurred while flushing command handler: {}.", handler, exc);
      }
    }
    return registries;
  }

  private Set<CommandRegistry<?>> flushTextCommands(Map<Class<?>, CommandHandler<?>> handlers) {
    final Set<CommandRegistry<?>> registries = new HashSet<>();
    for (CommandPayload<?> payload : textcommands) {
      final CommandAttributes attributes = payload.attrs();
      final Class<? extends CommandHandler<?>> command = payload.command();
      try {
        final CommandHandler<?> handler = Objects.requireNonNull(handlers.get(command));
        registries.add(this.flushTextCommand(attributes, handler));
        LOGGER.info("Success to flush command: {} ({}). The content is available for requests.", command, attributes.getName());
      } catch (Exception exc) {
        LOGGER.error("An unexpected error occurred while flushing command: {} ({}).", command, attributes.getName(), exc);
      }
    }
    return registries;
  }

  @SuppressWarnings("unchecked")
  private <T> SlashCommandRegistry<T> flushSlashCommand(SlashCommandAttributes attributes,CommandHandler<T> slashcommand)
    throws ArgumentParserException {
    SlashMapperRegistry<T> mapper = (SlashMapperRegistry<T>)parsers.getSlashMapper(attributes.getArgumentClass());
    if (Objects.isNull(mapper))
      throw new ArgumentParserException("");
    return new SlashCommandRegistry<>(slashcommand, mapper, attributes);
  }

  private Set<SlashCommandRegistry<?>> flushSlashCommands(Map<Class<?>, CommandHandler<?>> handlers) {
    final Set<SlashCommandRegistry<?>> registries = new HashSet<>();
    for (SlashCommandPayload<?> payload : slashcommands) {
      final SlashCommandAttributes attributes = payload.attrs();
      final Class<? extends CommandHandler<?>> slashcommand = payload.slashcommand();
      try {
        final CommandHandler<?> handler = Objects.requireNonNull(handlers.get(slashcommand));
        registries.add(this.flushSlashCommand(attributes, handler));
        LOGGER.info("Success to flush slash command: {} ({}). The content is available for requests.", slashcommand, attributes.getName());
      } catch (Exception exc) {
        LOGGER.error("An unexpected error occurred while flushing command: {} ({}).", slashcommand, attributes.getName(), exc);
      }
    }
    return registries;
  }

  public void prepare(ParsersStaticRegistries staticParsers, ActionsStaticRegistries staticActions) {
    this.parsers = staticParsers;
    this.actions = staticActions;
  }

  @Override
  public CommandsStaticRegistries flush() {
    LOGGER.info("Flushing all commands and slash commands to generate static content.");
    CommandsInitialization init = new CommandsInitialization(parsers, actions);
    final Map<Class<?>, CommandHandler<?>> handlers = this.flushCommandHandler(init);
    final Set<CommandRegistry<?>> textcommands = this.flushTextCommands(handlers);
    final Set<SlashCommandRegistry<?>> slashcommands = this.flushSlashCommands(handlers);
    LOGGER.info("Total of {} text commands and {} slash commands flushed successfully.",
      textcommands.size(), slashcommands.size());
    return new CommandsStaticRegistries(textcommands, slashcommands);
  }
}
