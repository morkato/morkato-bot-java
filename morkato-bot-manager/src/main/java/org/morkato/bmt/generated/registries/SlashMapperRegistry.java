package org.morkato.bmt.generated.registries;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.morkato.bmt.components.SlashMapper;

import java.util.Collection;

public class SlashMapperRegistry<T> {
  private final SlashMapper<T> internal;
  public SlashMapperRegistry(SlashMapper<T> mapper) {
    this.internal = mapper;
  }

  public T mapInteraction(CommandInteraction interaction) {
    return internal.mapInteraction(interaction);
  }

  public Collection<OptionData> createOptions() {
    return internal.createOptions();
  }
}
