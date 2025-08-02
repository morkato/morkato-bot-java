package org.morkato.bot;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface PagedEmbed {
  MessageEmbed build(int page);
  int size();
}
