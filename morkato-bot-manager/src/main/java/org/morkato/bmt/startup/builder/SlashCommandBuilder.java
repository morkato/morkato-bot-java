package org.morkato.bmt.startup.builder;

public interface SlashCommandBuilder<T> {
  SlashCommandBuilder<T> setName(String name);
  SlashCommandBuilder<T> setDescription(String description);
  SlashCommandBuilder<T> deferReply(boolean ep);
  void queue();

  default SlashCommandBuilder<T> deferReply() {
    return this.deferReply(false);
  }
}
