package org.morkato.bmt.internal.registration;

import org.morkato.bmt.components.*;
import org.morkato.bmt.internal.registration.builder.*;
import org.morkato.bmt.registration.builder.SlashCommandBuilder;
import org.morkato.bmt.registration.builder.TextCommandBuilder;
import org.morkato.bmt.registration.AppCommandTree;
import org.morkato.bmt.registration.builder.CommandExceptionBuilder;
import org.morkato.bmt.registration.builder.ObjectParserBuilder;
import org.morkato.bmt.registration.builder.SlashMapperBuilder;
import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.registration.payload.SlashCommandPayload;
import org.morkato.boot.Extension;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AppCommandTreeInternal implements AppCommandTree {
  private final Extension<BotContext> extension;
  private final Set<CommandPayload<?>> commands = new HashSet<>();
  private final Set<SlashCommandPayload<?>> slashcommands = new HashSet<>();
  private final Set<SlashMapper<?>> slashmappers = new HashSet<>();
  private final Set<ObjectParser<?>> parsers = new HashSet<>();
  private final Set<CommandException<?>> commandsExceptions = new HashSet<>();

  public AppCommandTreeInternal(Extension<BotContext> extension) {
    this.extension = Objects.requireNonNull(extension);
  }

  @Override
  public <T> TextCommandBuilder<T> text(Command<T> command) {
    return new TextCommandBuilderInternal<>(this, extension, command);
  }

  @Override
  public <T> SlashCommandBuilder<T> slash(Command<T> slashcommand) {
    return new SlashCommandBuilderInternal<>(this, null, slashcommand);
  }

  @Override
  public <T> SlashMapperBuilder<T> slashMapper(SlashMapper<T> mapper) {
    return new SlashMapperBuilderInternal<>(this, extension, mapper);
  }

  @Override
  public <T> ObjectParserBuilder<T> objectParser(ObjectParser<T> objectparser) {
    return new ObjectParserBuilderInternal<>(this, extension, objectparser);
  }

  @Override
  public <T extends Throwable> CommandExceptionBuilder<T> textExceptionHandler(CommandException<T> exceptionhandler) {
    return new CommandExceptionBuilderInternal<>(this, extension, exceptionhandler);
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
  public Set<CommandException<?>> getPendingCommandExceptions() {
    return commandsExceptions;
  }

  @Override
  public Set<SlashMapper<?>> getSlashCommandMappers(){
    return slashmappers;
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

  public <T extends Throwable> void addCommandExceptionHandler(CommandException<T> handler) {
    this.commandsExceptions.add(handler);
  }

  public <T> void addSlashMapper(SlashMapper<T> slashmapper) {
    slashmappers.add(slashmapper);
  }
}
