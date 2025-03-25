package org.morkato.bmt;

import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.registration.RegisterManagement;
import org.morkato.bmt.components.MessageEmbedBuilder;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.registration.registries.*;
import java.util.Iterator;
import java.util.Arrays;

public class ApplicationRegistries implements Shutdownable {
  private final ExtensionRegistry[] extensions;
  private final MessageEmbedBuilderRegistry[] embeds;
  private final ArgumentRegistry[] parsers;
  private final TextCommandExceptionRegistry[] exceptions;
  private final CommandRegistry<?>[] commands;

  public ApplicationRegistries(
    RegisterManagement<ExtensionRegistry, Extension> extensions,
    RegisterManagement<MessageEmbedBuilderRegistry, MessageEmbedBuilder<?>> embeds,
    RegisterManagement<ArgumentRegistry, ObjectParser<?>> arguments,
    RegisterManagement<TextCommandExceptionRegistry, CommandException<?>> bounders,
    RegisterManagement<CommandRegistry<?>, Command<?>> commands
  ) {
    this.extensions = new ExtensionRegistry[extensions.size()];
    this.embeds = new MessageEmbedBuilderRegistry[embeds.size()];
    this.parsers = new ArgumentRegistry[arguments.size()];
    this.exceptions = new TextCommandExceptionRegistry[bounders.size()];
    this.commands = new CommandRegistry[commands.size()];
    final int maxSize = Arrays.stream(new int[] {
      this.extensions.length,
      this.embeds.length,
      this.parsers.length,
      this.exceptions.length,
      this.commands.length
    }).max().orElse(0);
    if (maxSize == 0)
      return;
    final Iterator<ExtensionRegistry> extensionIterator = extensions.registries().iterator();
    final Iterator<MessageEmbedBuilderRegistry> embedsIterator = embeds.registries().iterator();
    final Iterator<ArgumentRegistry> argumentsIterator = arguments.registries().iterator();
    final Iterator<TextCommandExceptionRegistry> exceptionIterator = bounders.registries().iterator();
    final Iterator<CommandRegistry<?>> commandIterator = commands.registries().iterator();
    for (int i = 0; i < maxSize; ++i) {
      if (extensionIterator.hasNext())
        this.extensions[i] = extensionIterator.next();
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
    return
      this.extensions.length
      + this.embeds.length
      + this.parsers.length
      + this.exceptions.length
      + this.commands.length;
  }

  public MessageEmbedBuilderRegistry[] getRegisteredEmbeds() {
    return Arrays.copyOf(embeds, embeds.length);
  }

  public ArgumentRegistry[] getRegisteredParsers() {
    return Arrays.copyOf(parsers, parsers.length);
  }

  public TextCommandExceptionRegistry[] getRegisteredExceptions() {
    return Arrays.copyOf(exceptions, exceptions.length);
  }

  public CommandRegistry<?>[] getRegisteredCommands() {
    return Arrays.copyOf(commands, commands.length);
  }

  public void clear() {
    Arrays.fill(extensions, null);
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
