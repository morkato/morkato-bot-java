package org.morkato.bmt.startup.builder;

import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;

public interface ButtonActionBuilder {
  ButtonActionBuilder label(String label);
  ButtonActionBuilder style(ButtonStyle style);
  ButtonActionBuilder disabled();
  ButtonActionBuilder enabled();
  ButtonActionBuilder emoji(EmojiUnion emoji);
  void queue();
}
