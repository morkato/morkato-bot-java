package org.morkato.bmt.generated;

import org.morkato.bmt.generated.registries.SlashCommandRegistry;
import org.morkato.bmt.registration.MapRegistryManagement;

import java.util.HashMap;
import java.util.Map;

public class SlashCommandStaticRegistries implements MapRegistryManagement<String, SlashCommandRegistry<?>> {
  private Map<String, SlashCommandRegistry<?>> items = new HashMap<>();
  public SlashCommandStaticRegistries(ApplicationStaticRegistries registries) {
    for (SlashCommandRegistry<?> registry : registries.getRegisteredSlashCommands())
      items.put(registry.getName(), registry);
  }

  @Override
  public SlashCommandRegistry<?> get(String key) {
    return items.get(key);
  }
}
