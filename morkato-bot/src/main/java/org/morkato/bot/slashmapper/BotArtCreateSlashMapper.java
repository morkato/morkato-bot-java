package org.morkato.bot.slashmapper;

import org.morkato.api.entity.art.ArtType;
import org.morkato.api.repository.art.ArtCreationQuery;
import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.commands.rules.SlashMapperData;
import org.morkato.bmt.commands.rules.SlashMappingInteraction;

public class BotArtCreateSlashMapper implements SlashMapper<ArtCreationQuery> {
  private static ArtType mapType(String type) {
    return switch (type) {
      case "RESPIRATION" -> ArtType.RESPIRATION;
      case "KEKKIJUTSU" -> ArtType.KEKKIJUTSU;
      case "FIGHTING_STYLE" -> ArtType.FIGHTING_STYLE;
      default -> throw new RuntimeException("A stupid impossible error.");
    };
  }
  @Override
  public void createOptions(SlashMappingInteraction interaction) {
    interaction.asString("name")
      .setRequired()
      .queue();
    interaction.asString("type")
      .setRequired()
      .addChoice("RESPIRATION", "RESPIRATION")
      .addChoice("KEKKIJUTSU", "KEKKIJUTSU")
      .addChoice("FIGHTING_STYLE", "FIGHTING_STYLE")
      .queue();
    interaction.asString("description")
      .queue();
    interaction.asString("banner")
      .queue();
  }

  @Override
  public ArtCreationQuery mapInteraction(SlashMapperData payload) {
    final String name = payload.getAsString("name");
    final ArtType type = mapType(payload.getAsString("type"));
    final String description = payload.getAsStringOrNull("description");
    final String banner = payload.getAsStringOrNull("banner");
    return new ArtCreationQuery()
      .setName(name)
      .setType(type)
      .setDescription(description)
      .setBanner(banner);
  }
}
