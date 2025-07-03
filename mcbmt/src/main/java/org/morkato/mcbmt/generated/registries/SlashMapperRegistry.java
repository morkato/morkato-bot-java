package org.morkato.mcbmt.generated.registries;

import org.morkato.mcbmt.components.SlashMapper;
import org.morkato.mcbmt.commands.rules.SlashMapperData;
import org.morkato.mcbmt.commands.rules.SlashMappingInteraction;

public class SlashMapperRegistry<T> {
  private final SlashMapper<T> internal;
  public SlashMapperRegistry(SlashMapper<T> mapper) {
    this.internal = mapper;
  }

  public T mapInteraction(SlashMapperData data) {
    return internal.mapInteraction(data);
  }

  public void createOptions(SlashMappingInteraction interaction) {
    internal.createOptions(interaction);
  }
}
