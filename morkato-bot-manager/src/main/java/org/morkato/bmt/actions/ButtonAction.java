package org.morkato.bmt.actions;

import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.Objects;

public interface ButtonAction {
  class ButtonActionBuilder {
    private String label;
    private ButtonStyle style;
    private boolean disabled;
    private EmojiUnion emoji;

    public ButtonActionBuilder label(String label) {
      this.label = label;
      return this;
    }
    public ButtonActionBuilder style(ButtonStyle style) {
      this.style = style;
      return this;
    }
    public ButtonActionBuilder disabled() {
      this.disabled = true;
      return this;
    }
    public ButtonActionBuilder enabled() {
      this.disabled = false;
      return this;
    }
    public ButtonActionBuilder emoji(EmojiUnion emoji) {
      this.emoji = emoji;
      return this;
    }
    public ButtonAction build() {
      return new ButtonActionImpl(
        label,
        style,
        disabled,
        emoji
      );
    }
  }

  class ButtonActionImpl implements ButtonAction {
    private final String label;
    private final ButtonStyle style;
    private final boolean disabled;
    private final EmojiUnion emoji;

    public ButtonActionImpl(
      String label,
      ButtonStyle style,
      boolean disabled,
      EmojiUnion emoji
    ) {
      this.label = label;
      this.style = Objects.requireNonNull(style);
      this.disabled = disabled;
      this.emoji = emoji;
    }

    @Override
    public String getLabel() {
      return label;
    }

    @Override
    public ButtonStyle getStyle() {
      return style;
    }

    @Override
    public boolean isDisabled() {
      return disabled;
    }

    @Override
    public boolean isEnabled() {
      return !disabled;
    }

    @Override
    public EmojiUnion getEmoji() {
      return emoji;
    }
  }

  static ButtonActionBuilder builder() {
    return new ButtonActionBuilder();
  }

  String getLabel();
  ButtonStyle getStyle();
  boolean isDisabled();
  boolean isEnabled();
  EmojiUnion getEmoji();
}
