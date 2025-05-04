package org.morkato.bot.slashmapper;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.morkato.bmt.components.SlashMapper;
import java.util.Collection;
import java.util.Objects;
import java.util.List;

public class TestMapper implements SlashMapper<String> {
  @Override
  public Collection<OptionData> createOptions() {
    return List.of(
      new OptionData(OptionType.STRING, "test", "...")
    );
  }

  @Override
  public String mapInteraction(CommandInteraction interaction) {
    return Objects.requireNonNull(interaction.getOption("test")).getAsString();
  }
}
