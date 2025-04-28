package org.morkato.bmt.internal.context;

import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.internal.context.builder.TextCommandBuilderInternal;
import org.morkato.bmt.context.builder.TextCommandBuilder;
import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.components.MessageEmbedBuilder;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.hooks.NamePointer;
import org.morkato.boot.Extension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BotContextInternal implements BotContext {
  private final Set<CommandPayload<?>> commands = new HashSet<>();
  private final Set<NamePointer> names = new HashSet<>();
  private final Set<ObjectParser<?>> arguments = new HashSet<>();
  private final Set<CommandException<?>> exceptions = new HashSet<>();
  private final Extension<BotContext> extension;

  public BotContextInternal(Extension<BotContext> extension) {
    this.extension = extension;
  }

  public void addCommand(CommandPayload<?> command) {
    commands.add(command);
  }

  public void addCommandName(NamePointer pointer) {
    names.add(pointer);
  }

  @Override
  public <T> TextCommandBuilder<T> registerCommand(Command<T> command) {
    return new TextCommandBuilderInternal<>(this, extension, command);
  }

  @Override
  public <T> TextCommandBuilder<T> registerCommand(String name, Command<T> command) {
    return this.registerCommand(command)
      .addName(name);
  }

  @Override
  public void registerArgument(ObjectParser<?> parser) {
    arguments.add(parser);
  }

  @Override
  public void registerEmbed(MessageEmbedBuilder<?> embedBuilder) {
    throw new RuntimeException("Not yet implemented.");
  }

  @Override
  public void registerCommandException(CommandException<?> exception) {
    exceptions.add(exception);
  }

  @Override
  public void registerPointer(NamePointer pointer) {
    throw new RuntimeException("Not yet implemented.");
  }

  @Override
  public Set<CommandPayload<?>> getPendingCommands() {
    return commands;
  }

  @Override
  public Set<ObjectParser<?>> getPendingArguments() {
    return arguments;
  }

  @Override
  public Set<MessageEmbedBuilder<?>> getPendingEmbeds() {
    return Set.of();
  }

  @Override
  public Set<CommandException<?>> getPendingCommandExceptions() {
    return exceptions;
  }

  @Override
  public Set<NamePointer> getPendingPointers() {
    return Set.of();
  }

  @Override
  public Map<Class<?>, Set<?>> bind() {
    final Map<Class<?>, Set<?>> components = new HashMap<>();
    components.put(CommandPayload.class, commands);
    components.put(ObjectParser.class, arguments);
    components.put(CommandException.class, exceptions);
    return components;
  }
}
