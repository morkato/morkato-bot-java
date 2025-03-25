package org.morkato.bot;

import org.morkato.bmt.ApplicationRegistries;
import org.morkato.bmt.registration.registries.CommandRegistry;
import org.morkato.bmt.registration.MapRegistryManagement;

import java.util.HashMap;
import java.util.Map;

public class CommandsStaticRegistries implements MapRegistryManagement<String, CommandRegistry<?>> {
  private final Map<String, CommandRegistry<?>> registries = new HashMap<>();

  public CommandsStaticRegistries(ApplicationRegistries registries) {
    for (CommandRegistry<?> registry : registries.getRegisteredCommands()) {
      this.registries.put(registry.getCommandClassName(), registry);
    }
  }

  @Override
  public CommandRegistry<?> get(String key) {
    return registries.get(key);
  }
}
