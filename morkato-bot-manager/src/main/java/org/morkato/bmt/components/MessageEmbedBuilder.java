package org.morkato.bmt.components;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface MessageEmbedBuilder<T> {
  MessageEmbed build(EmbedBuilder builder, T dependency);
}
