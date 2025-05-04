package org.morkato.bmt.registration.builder;

public interface SlashCommandBuilder<T> {
  SlashCommandBuilder<T> setName(String name);
  SlashCommandBuilder<T> setDescription(String description);
  void queue();
}
