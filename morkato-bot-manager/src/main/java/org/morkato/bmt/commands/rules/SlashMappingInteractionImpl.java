package org.morkato.bmt.commands.rules;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.LinkedHashSet;
import java.util.Set;

public class SlashMappingInteractionImpl implements SlashMappingInteraction {
  private static class SlashOptionBuilderImpl implements SlashOptionBuilder {
    private final SlashMappingInteractionImpl options;
    private final Set<Command.Choice> choices = new LinkedHashSet<>();
    private final String name;
    private final OptionType type;
    private boolean required = false;
    private String description;
    private boolean flushed = false;

    public SlashOptionBuilderImpl(
      SlashMappingInteractionImpl options,
      String name,
      OptionType type
    ) {
      this.options = options;
      this.name = name;
      this.type = type;
    }

    @Override
    public SlashOptionBuilder setDescription(String desc) {
      this.description = desc;
      return this;
    }

    @Override
    public SlashOptionBuilder setRequired() {
      required = true;
      return this;
    }

    @Override
    public SlashOptionBuilder addChoice(String name, String value) {
      choices.add(new Command.Choice(name, value));
      return this;
    }

    @Override
    public void queue() {
      if (flushed)
        return;
      final OptionData option = new OptionData(type, name, description == null ? "..." : description)
        .setRequired(required)
        .addChoices(choices);
      this.options.add(option);
      flushed = true;
    }
  }

  private final Set<OptionData> options = new LinkedHashSet<>();

  private void add(OptionData data) {
    this.options.add(data);
  }

  @Override
  public SlashOptionBuilder asString(String name) {
    return new SlashOptionBuilderImpl(this, name, OptionType.STRING);
  }

  @Override
  public SlashOptionBuilder asLong(String name) {
    return new SlashOptionBuilderImpl(this, name, OptionType.NUMBER);
  }

  public OptionData[] build() {
    return options.toArray(OptionData[]::new);
  }
}
