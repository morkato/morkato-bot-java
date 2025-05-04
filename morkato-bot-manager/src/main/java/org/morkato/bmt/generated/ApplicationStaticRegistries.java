package org.morkato.bmt.generated;

import org.morkato.bmt.Shutdownable;
import org.morkato.bmt.generated.registries.CommandExceptionRegistry;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.generated.registries.ObjectParserRegistry;
import org.morkato.bmt.generated.registries.SlashMapperRegistry;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;
import org.morkato.bmt.registration.*;

import java.util.Iterator;
import java.util.Arrays;

public class ApplicationStaticRegistries implements Shutdownable {
  public static String representation(ApplicationStaticRegistries registries) {
    return ApplicationStaticRegistries.class.getName() + "[commands=" + Arrays.toString(registries.commands) + ", slashcommands=" + Arrays.toString(registries.slashcommands) + "]";
  }

  private final ObjectParserRegistry<?>[] parsers;
  private final CommandExceptionRegistry<?>[] exceptions;
  private final CommandRegistry<?>[] commands;
  private final SlashCommandRegistry<?>[] slashcommands;
  private final SlashMapperRegistry<?>[] slashmappers;

  public ApplicationStaticRegistries(
    RegisterInfo<ObjectParserRegistry<?>> arguments,
    RegisterInfo<CommandExceptionRegistry<?>> bounders,
    RegisterInfo<CommandRegistry<?>> commands,
    RegisterInfo<SlashCommandRegistry<?>> slashcommands,
    RegisterInfo<SlashMapperRegistry<?>> slashmappers
  ) {
    this.parsers = new ObjectParserRegistry<?>[arguments.size()];
    this.exceptions = new CommandExceptionRegistry<?>[bounders.size()];
    this.commands = new CommandRegistry<?>[commands.size()];
    this.slashcommands = new SlashCommandRegistry<?>[slashcommands.size()];
    this.slashmappers = new SlashMapperRegistry<?>[slashmappers.size()];
    final int maxSize = Arrays.stream(new int[] {
      this.parsers.length,
      this.exceptions.length,
      this.commands.length,
      this.slashcommands.length,
      this.slashmappers.length
    }).max().orElse(0);
    if (maxSize == 0)
      return;
    final Iterator<ObjectParserRegistry<?>> argumentsIterator = arguments.registries().iterator();
    final Iterator<CommandExceptionRegistry<?>> exceptionIterator = bounders.registries().iterator();
    final Iterator<CommandRegistry<?>> commandIterator = commands.registries().iterator();
    final Iterator<SlashCommandRegistry<?>> slashcommandIterator = slashcommands.registries().iterator();
    final Iterator<SlashMapperRegistry<?>> slashmappersIterator = slashmappers.registries().iterator();
    for (int i = 0; i < maxSize; ++i) {
      if (argumentsIterator.hasNext())
        this.parsers[i] = argumentsIterator.next();
      if (exceptionIterator.hasNext())
        this.exceptions[i] = exceptionIterator.next();
      if (commandIterator.hasNext())
        this.commands[i] = commandIterator.next();
      if (slashcommandIterator.hasNext())
        this.slashcommands[i] = slashcommandIterator.next();
      if (slashmappersIterator.hasNext())
        this.slashmappers[i] = slashmappersIterator.next();
    }
  }

  public int totally() {
    return
      this.parsers.length
      + this.exceptions.length
      + this.commands.length
      + this.slashcommands.length
      + this.slashmappers.length;
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

  public SlashCommandRegistry<?>[] getRegisteredSlashCommands() {
    return Arrays.copyOf(slashcommands, slashcommands.length);
  }

  public void clear() {
    Arrays.fill(parsers, null);
    Arrays.fill(exceptions, null);
    Arrays.fill(commands, null);
    Arrays.fill(slashcommands, null);
    Arrays.fill(slashmappers, null);
  }

  @Override
  public void shutdown() {
    this.clear();
  }
}
