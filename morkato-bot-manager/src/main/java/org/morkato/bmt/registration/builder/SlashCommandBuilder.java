package org.morkato.bmt.registration.builder;

import org.morkato.bmt.action.MessageCreation;

public interface SlashCommandBuilder<T> {
  SlashCommandBuilder<T> setName(String name);
  SlashCommandBuilder<T> setDescription(String description);
  SlashCommandBuilder<T> deferReply();
  void queue();

}
