package org.morkato.bmt.generated.registries;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.context.SlashMapperData;
import org.morkato.bmt.context.SlashMappingInteraction;

import java.util.Collection;

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
