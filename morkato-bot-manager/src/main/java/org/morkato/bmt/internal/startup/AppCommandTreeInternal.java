package org.morkato.bmt.internal.startup;

import org.morkato.bmt.internal.startup.builder.*;
import org.morkato.bmt.startup.payload.SlashCommandPayload;
import org.morkato.bmt.startup.payload.CommandPayload;
import org.morkato.bmt.components.*;
import org.morkato.bmt.startup.builder.SlashCommandBuilder;
import org.morkato.bmt.startup.builder.TextCommandBuilder;
import org.morkato.bmt.startup.builder.CommandExceptionBuilder;
import org.morkato.bmt.startup.builder.ObjectParserBuilder;
import org.morkato.bmt.startup.builder.SlashMapperBuilder;
import org.morkato.bmt.startup.AppCommandTree;
import org.morkato.bmt.context.BotContext;
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
  private final Set<CommandExceptionHandler<?>> commandsExceptions = new HashSet<>();

  public AppCommandTreeInternal(Extension<BotContext> extension) {
    this.extension = Objects.requireNonNull(extension);
  }

  @Override
  public <T> TextCommandBuilder<T> text(CommandHandler<T> command) {
    return new TextCommandBuilderInternal<>(this, extension, command);
  }

  @Override
  public <T> SlashCommandBuilder<T> slash(CommandHandler<T> slashcommand) {
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
  public <T extends Throwable> CommandExceptionBuilder<T> exceptionHandler(CommandExceptionHandler<T> exceptionhandler) {
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
  public Set<CommandExceptionHandler<?>> getPendingCommandExceptions() {
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

  public <T extends Throwable> void addCommandExceptionHandler(CommandExceptionHandler<T> handler) {
    this.commandsExceptions.add(handler);
  }

  public <T> void addSlashMapper(SlashMapper<T> slashmapper) {
    slashmappers.add(slashmapper);
  }
}
