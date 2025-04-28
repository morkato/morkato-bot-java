package org.morkato.bmt.context;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.MessageEmbedBuilder;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.hooks.NamePointer;
import org.morkato.bmt.context.builder.TextCommandBuilder;
import org.morkato.bmt.registration.payload.CommandPayload;

import java.util.Map;
import java.util.Set;

public interface BotContext{
  <T> TextCommandBuilder<T> registerCommand(Command<T> command);
  <T> TextCommandBuilder<T> registerCommand(String name, Command<T> command);
  void registerArgument(ObjectParser<?> parser);
  void registerEmbed(MessageEmbedBuilder<?> embedBuilder);
  void registerCommandException(CommandException<?> exception);
  void registerPointer(NamePointer pointer);

  Set<CommandPayload<?>> getPendingCommands();
  Set<ObjectParser<?>> getPendingArguments();
  Set<MessageEmbedBuilder<?>> getPendingEmbeds();
  Set<CommandException<?>> getPendingCommandExceptions();
  Set<NamePointer> getPendingPointers();

  Map<Class<?>, Set<?>> bind();
}
