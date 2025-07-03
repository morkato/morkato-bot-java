package org.morkato.mcbmt.commands.rules;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SlashMapperDataImpl implements SlashMapperData {
  private final CommandInteraction interaction;
  public SlashMapperDataImpl(CommandInteraction interaction) {
    this.interaction = Objects.requireNonNull(interaction);
  }

  private OptionMapping getOptionOrNull(String name) {
    return interaction.getOption(name);
  }

  @Nonnull
  private OptionMapping getOption(String name) {
    final OptionMapping option = this.getOptionOrNull(name);
    if (Objects.isNull(option))
      throw new IllegalArgumentException("Option with name: " + name + " not has registered.");
    return option;
  }

  @Override
  public String getAsString(String key) {
    return this.getOption(key).getAsString();
  }

  @Override
  public long getAsLong(String key) {
    return this.getOption(key).getAsLong();
  }

  @Override
  public String getAsStringOrNull(String key) {
    final OptionMapping option = this.getOptionOrNull(key);
    if (Objects.isNull(option))
      return null;
    return option.getAsString();
  }

  @Override
  public long getAsLongOrNull(String key) {
    final OptionMapping option = this.getOptionOrNull(key);
    if (Objects.isNull(option))
      return 0L;
    return option.getAsLong();
  }
}