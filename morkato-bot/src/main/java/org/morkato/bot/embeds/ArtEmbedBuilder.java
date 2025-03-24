package org.morkato.bot.embeds;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.morkato.bmt.components.MessageEmbedBuilder;
import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.attack.Attack;
import org.morkato.bmt.annotation.Component;
import org.morkato.bot.content.EmbedContent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.morkato.api.entity.art.Art;
import java.text.MessageFormat;
import java.util.Objects;

@Component
public class ArtEmbedBuilder implements MessageEmbedBuilder<Art> {
  private String getTitle(Art art) {
    return switch (art.getType()) {
      case RESPIRATION -> EmbedContent.RESPIRATION_TITLE;
      case KEKKIJUTSU -> EmbedContent.KEKKIJUTSU_TITLE;
      case FIGHTING_STYLE -> EmbedContent.FIGHTING_STYLE_TITLE;
    };
  }
  @Override
  public MessageEmbed build(EmbedBuilder builder, Art art) {
    final ObjectResolver<Attack> attacks = art.getAttackResolver();
    System.out.println(this.getTitle(art));
    final String title = MessageFormat.format(this.getTitle(art), art.getName());
    String description = art.getDescription();
    if (Objects.isNull(description))
      description = EmbedContent.NO_DESCRIPTION;
    if (!attacks.isEmpty()) {
      description += "\n\n";
      description += "ataques";
    }
    return builder
      .setTitle(title)
      .setDescription(description)
      .build();
  }
}
