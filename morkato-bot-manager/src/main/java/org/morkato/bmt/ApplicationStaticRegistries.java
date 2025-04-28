package org.morkato.bmt;

import org.morkato.bmt.registration.RegisterInfo;
import org.morkato.bmt.registration.CommandExceptionRegistry;
import org.morkato.bmt.registration.CommandRegistry;
import org.morkato.bmt.registration.ObjectParserRegistry;

import java.util.Iterator;
import java.util.Arrays;

public class ApplicationStaticRegistries implements Shutdownable {
  private final ObjectParserRegistry<?>[] parsers;
  private final CommandExceptionRegistry[] exceptions;
  private final CommandRegistry<?>[] commands;

  public ApplicationStaticRegistries(
    RegisterInfo<ObjectParserRegistry<?>> arguments,
    RegisterInfo<CommandExceptionRegistry<?>> bounders,
    RegisterInfo<CommandRegistry<?>> commands
  ) {
    this.parsers = new ObjectParserRegistry<?>[arguments.size()];
    this.exceptions = new CommandExceptionRegistry[bounders.size()];
    this.commands = new CommandRegistry[commands.size()];
    final int maxSize = Arrays.stream(new int[] {
      this.parsers.length,
      this.exceptions.length,
      this.commands.length
    }).max().orElse(0);
    if (maxSize == 0)
      return;
    final Iterator<ObjectParserRegistry<?>> argumentsIterator = arguments.registries().iterator();
    final Iterator<CommandExceptionRegistry<?>> exceptionIterator = bounders.registries().iterator();
    final Iterator<CommandRegistry<?>> commandIterator = commands.registries().iterator();
    for (int i = 0; i < maxSize; ++i) {
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
      this.parsers.length
      + this.exceptions.length
      + this.commands.length;
  }

  public ObjectParserRegistry<?>[] getRegisteredParsers() {
    return Arrays.copyOf(parsers, parsers.length);
  }

  public CommandExceptionRegistry<?>[] getRegisteredExceptions() {
    return Arrays.copyOf(exceptions, exceptions.length);
  }

  public CommandRegistry<?>[] getRegisteredCommands() {
    return Arrays.copyOf(commands, commands.length);
  }

  public void clear() {
    Arrays.fill(parsers, null);
    Arrays.fill(exceptions, null);
    Arrays.fill(commands, null);
  }

  @Override
  public void shutdown() {
    this.clear();
  }
}
