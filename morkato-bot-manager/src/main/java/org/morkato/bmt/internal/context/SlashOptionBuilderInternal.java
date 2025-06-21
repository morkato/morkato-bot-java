package org.morkato.bmt.internal.context;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.morkato.bmt.context.SlashOptionBuilder;
import java.util.HashSet;
import java.util.Set;

public class SlashOptionBuilderInternal implements SlashOptionBuilder {
  private final SlashMappingInteractionInternal options;
  private final Set<Command.Choice> choices = new HashSet<>();
  private final String name;
  private final OptionType type;
  private boolean required = false;
  private String description;
  private boolean flushed = false;

  public SlashOptionBuilderInternal(SlashMappingInteractionInternal options, String name, OptionType type) {
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
