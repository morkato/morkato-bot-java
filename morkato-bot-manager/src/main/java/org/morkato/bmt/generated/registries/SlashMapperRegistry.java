package org.morkato.bmt.generated.registries;

import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.commands.rules.SlashMapperData;
import org.morkato.bmt.commands.rules.SlashMappingInteraction;

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
