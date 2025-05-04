package org.morkato.bmt.generated;

import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.registration.MapRegistryManagement;

import java.util.HashMap;
import java.util.Map;

public class CommandsStaticRegistries implements MapRegistryManagement<String,CommandRegistry<?>> {
  private final Map<String, CommandRegistry<?>> registries = new HashMap<>();

  public CommandsStaticRegistries(ApplicationStaticRegistries registries) {
    CommandRegistry<?>[] commands = registries.getRegisteredCommands();
    for (CommandRegistry<?> registry : commands) {
      this.registries.put(registry.getName(), registry);
      for (String alias : registry.getAliases())
        this.registries.put(alias, registry);
    }
  }

  @Override
  public CommandRegistry<?> get(String key) {
    return registries.get(key);
  }
}
