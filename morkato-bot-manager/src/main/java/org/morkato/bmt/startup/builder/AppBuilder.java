package org.morkato.bmt.startup.builder;

import org.morkato.bmt.startup.payload.SlashCommandPayload;
import org.morkato.bmt.startup.payload.CommandPayload;
import org.morkato.bmt.startup.payload.ActionPayload;
import org.morkato.bmt.components.*;
import org.morkato.bmt.BotContext;
import org.morkato.boot.Extension;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public interface AppBuilder {
  static class AppBuilderImpl implements AppBuilder {
    private final Extension<BotContext> extension;
    private final Set<CommandHandler<?>> handlers = new HashSet<>();
    private final Set<CommandPayload<?>> commands = new HashSet<>();
    private final Set<SlashCommandPayload<?>> slashcommands = new HashSet<>();
    private final Set<ActionPayload<?>> actions = new HashSet<>();
    private final Set<SlashMapper<?>> slashmappers = new HashSet<>();
    private final Set<ObjectParser<?>> parsers = new HashSet<>();
    private final Set<CommandExceptionHandler<?>> commandsExceptions = new HashSet<>();

    public AppBuilderImpl(Extension<BotContext> extension) {
      this.extension = Objects.requireNonNull(extension);
    }
    @Override
    public void use(CommandHandler<?> handler) {
      handlers.add(handler);
    }

    @Override
    public <T> TextCommandBuilder<T> textCommand(Class<? extends CommandHandler<T>> command) {
      return new TextCommandBuilder<>(this, extension, command);
    }

    @Override
    public <T> SlashCommandBuilder<T> slashCommand(Class<? extends CommandHandler<T>> slashcommand) {
      return new SlashCommandBuilder<>(this, null, slashcommand);
    }

    @Override
    public <T> SlashMapperBuilder<T> slashMapper(SlashMapper<T> mapper) {
      return new SlashMapperBuilder<>(this, extension, mapper);
    }

    @Override
    public <T> ObjectParserBuilder<T> objectParser(ObjectParser<T> objectparser) {
      return new ObjectParserBuilder<>(this, extension, objectparser);
    }

    @Override
    public <T extends Throwable> CommandExceptionBuilder<T> commandExceptionHandler(CommandExceptionHandler<T> exceptionhandler) {
      return new CommandExceptionBuilder<>(this, extension, exceptionhandler);
    }

    @Override
    public <T> ActionBuilder<T> action(ActionHandler<T> actionhandler){
      return new ActionBuilder<>(this, extension, actionhandler);
    }

    @Override
    public Set<CommandPayload<?>> getPendingCommands() {
      return commands;
    }

    @Override
    public Set<SlashCommandPayload<?>> getPendingSlashCommands() {
      return slashcommands;
    }

    @Override
    public Set<ObjectParser<?>> getPendingArguments() {
      return parsers;
    }

    @Override
    public Set<CommandExceptionHandler<?>> getPendingCommandExceptions() {
      return commandsExceptions;
    }

    @Override
    public Set<SlashMapper<?>> getSlashCommandMappers(){
      return slashmappers;
    }

    @Override
    public Set<ActionPayload<?>> getPendingActions() {
      return actions;
    }

    @Override
    public Set<CommandHandler<?>> getPendingCommandHandlers(){
      return handlers;
    }

    public <T> void addCommand(CommandPayload<T> payload) {
      this.commands.add(payload);
    }

    public <T> void addSlashCommand(SlashCommandPayload<T> payload) {
      this.slashcommands.add(payload);
    }

    public <T> void addObjectParser(ObjectParser<T> parser) {
      this.parsers.add(parser);
    }

    public <T extends Throwable> void addCommandExceptionHandler(CommandExceptionHandler<T> handler) {
      this.commandsExceptions.add(handler);
    }

    public <T> void addSlashMapper(SlashMapper<T> slashmapper) {
      slashmappers.add(slashmapper);
    }

    public void addAction(ActionPayload<?> payload) {
      actions.add(payload);
    }
  }

  void use(CommandHandler<?> handler);
  <T> TextCommandBuilder<T> textCommand(Class<? extends CommandHandler<T>> command);
  <T> SlashCommandBuilder<T> slashCommand(Class<? extends CommandHandler<T>> slashcommand);
  <T> SlashMapperBuilder<T> slashMapper(SlashMapper<T> mapper);
  <T> ObjectParserBuilder<T> objectParser(ObjectParser<T> objectparser);
  <T extends Throwable> CommandExceptionBuilder<T> commandExceptionHandler(CommandExceptionHandler<T> exceptionhandler);
  <T> ActionBuilder<T> action(ActionHandler<T> actionHandler);

  Set<CommandPayload<?>> getPendingCommands();
  Set<SlashCommandPayload<?>> getPendingSlashCommands();
  Set<ObjectParser<?>> getPendingArguments();
  Set<CommandExceptionHandler<?>> getPendingCommandExceptions();
  Set<SlashMapper<?>> getSlashCommandMappers();
  Set<ActionPayload<?>> getPendingActions();
  Set<CommandHandler<?>> getPendingCommandHandlers();
}
