package org.morkato.bmt.generated;

import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;

import java.util.*;

public class CommandsStaticRegistries {
  private final CommandRegistry<?>[] textcommands;
  private final SlashCommandRegistry<?>[] slashcommands;
  private final Map<String, CommandRegistry<?>> mapTextCommands = new HashMap<>();
  private final Map<String, SlashCommandRegistry<?>> mapSlashCommands = new HashMap<>();

  public CommandsStaticRegistries(Set<CommandRegistry<?>> textcommands, Set<SlashCommandRegistry<?>> slashcommands) {
    this.textcommands = new CommandRegistry[textcommands.size()];
    this.slashcommands = new SlashCommandRegistry[slashcommands.size()];
    Iterator<CommandRegistry<?>> textcommandsIterator = textcommands.iterator();
    Iterator<SlashCommandRegistry<?>> slashcommandsIterator = slashcommands.iterator();
    for (int i = 0; i < this.textcommands.length; ++i) {
      CommandRegistry<?> registry = textcommandsIterator.next();
      this.textcommands[i] = registry;
      mapTextCommands.put(registry.getName(), registry);
      for (String name : registry.getAliases())
        mapTextCommands.put(name, registry);
    }
    for (int i = 0; i < this.slashcommands.length; ++i) {
      SlashCommandRegistry<?> registry = slashcommandsIterator.next();
      this.slashcommands[i] = registry;
      mapSlashCommands.put(registry.getName(), registry);
    }
  }

  public CommandRegistry<?> getTextCommand(String commandname) {
    return this.mapTextCommands.get(commandname);
  }

  public SlashCommandRegistry<?> getSlashCommand(String name) {
    return this.mapSlashCommands.get(name);
  }

  public CommandRegistry<?>[] getRegisteredTextCommands() {
    return Arrays.copyOf(textcommands, textcommands.length);
  }

  public SlashCommandRegistry<?>[] getRegisteredSlashCommands(){
    return Arrays.copyOf(slashcommands,slashcommands.length);
  }
}
