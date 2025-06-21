package org.morkato.bmt.internal.context;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.morkato.bmt.context.SlashMappingInteraction;
import org.morkato.bmt.context.SlashOptionBuilder;
import java.util.LinkedHashSet;
import java.util.Set;

public class SlashMappingInteractionInternal implements SlashMappingInteraction {
  private Set<OptionData> options = new LinkedHashSet<>();

  public void add(OptionData data) {
    this.options.add(data);
  }

  @Override
  public SlashOptionBuilder asString(String name) {
    return new SlashOptionBuilderInternal(this, name, OptionType.STRING);
  }

  @Override
  public SlashOptionBuilder asLong(String name) {
    return new SlashOptionBuilderInternal(this, name, OptionType.NUMBER);
  }

  public OptionData[] build() {
    return options.toArray(OptionData[]::new);
  }
}
