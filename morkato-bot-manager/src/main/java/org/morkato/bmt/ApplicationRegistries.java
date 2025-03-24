package org.morkato.bmt;

import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.registration.hooks.TextCommandExceptionRegistration;
import org.morkato.bmt.registration.hooks.MessageEmbedBuilderRegistration;
import org.morkato.bmt.registration.hooks.TextCommandRegistration;
import org.morkato.bmt.registration.hooks.ArgumentRegistration;
import org.morkato.bmt.components.MessageEmbedBuilder;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import java.util.Arrays;
import java.util.Iterator;

public class ApplicationRegistries implements Shutdownable {
  private final MessageEmbedBuilder<?>[] embeds;
  private final ObjectParser<?>[] parsers;
  private final CommandException<?>[] exceptions;
  private final CommandRegistry<?>[] commands;

  public ApplicationRegistries(
    MessageEmbedBuilderRegistration embeds,
    ArgumentRegistration arguments,
    TextCommandExceptionRegistration bounders,
    TextCommandRegistration commands
  ) {
    this.embeds = new MessageEmbedBuilder[embeds.size()];
    this.parsers = new ObjectParser[arguments.size()];
    this.exceptions = new CommandException[bounders.size()];
    this.commands = new CommandRegistry[commands.size()];
    final int maxSize = Arrays.stream(new int[] {
      this.embeds.length,
      this.parsers.length,
      this.exceptions.length,
      this.commands.length
    }).max().orElse(0);
    if (maxSize == 0)
      return;
    final Iterator<MessageEmbedBuilder<?>> embedsIterator = embeds.iterator();
    final Iterator<ObjectParser<?>> argumentsIterator = arguments.iterator();
    final Iterator<CommandException<?>> exceptionIterator = bounders.iterator();
    final Iterator<CommandRegistry<?>> commandIterator = commands.getRegistries().iterator();
    for (int i = 0; i < maxSize; ++i) {
      if (embedsIterator.hasNext())
        this.embeds[i] = embedsIterator.next();
      if (argumentsIterator.hasNext())
        this.parsers[i] = argumentsIterator.next();
      if (exceptionIterator.hasNext())
        this.exceptions[i] = exceptionIterator.next();
      if (commandIterator.hasNext())
        this.commands[i] = commandIterator.next();
    }
  }

  public int totally() {
    return this.embeds.length
      + this.parsers.length
      + this.exceptions.length
      + this.commands.length;
  }

  public void setCommandExceptionManager(MapRegistryManagement<Class<? extends Throwable>, CommandException<?>> mapper) {
    for (CommandRegistry<?> registry : this.commands) {
      registry.setExceptionManager(mapper);
    }
  }

  public MessageEmbedBuilder<?>[] getRegisteredEmbeds() {
    return Arrays.copyOf(embeds, embeds.length);
  }

  public ObjectParser<?>[] getRegisteredParsers() {
    return Arrays.copyOf(parsers, parsers.length);
  }

  public CommandException<?>[] getRegisteredExceptions() {
    return Arrays.copyOf(exceptions, exceptions.length);
  }

  public CommandRegistry<?>[] getRegisteredCommands() {
    return Arrays.copyOf(commands, commands.length);
  }

  public void clear() {
    Arrays.fill(embeds, null);
    Arrays.fill(parsers, null);
    Arrays.fill(exceptions, null);
    Arrays.fill(commands, null);
  }

  @Override
  public void shutdown() {
    this.clear();
  }
}
