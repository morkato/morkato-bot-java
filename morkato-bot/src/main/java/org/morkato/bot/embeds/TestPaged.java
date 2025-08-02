package org.morkato.bot.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.morkato.bot.PagedEmbed;
import org.morkato.bot.action.PagedEmbedAction;

public class TestPaged implements PagedEmbed {
  @Override
  public MessageEmbed build(int page) {
    return new EmbedBuilder()
      .setDescription("PagedEmbed: " + page)
      .build();
  }

  @Override
  public int size() {
    return 10;
  }
}
