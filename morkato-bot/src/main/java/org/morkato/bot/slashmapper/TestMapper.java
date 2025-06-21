package org.morkato.bot.slashmapper;

import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.context.SlashMapperData;
import org.morkato.bmt.context.SlashMappingInteraction;

public class TestMapper implements SlashMapper<String> {
  @Override
  public void createOptions(SlashMappingInteraction interaction) {
    interaction.asString("text")
      .setRequired()
      .queue();
  }

  @Override
  public String mapInteraction(SlashMapperData payload) {
    return payload.getAsString("text");
  }
}
