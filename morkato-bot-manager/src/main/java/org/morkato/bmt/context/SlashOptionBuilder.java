package org.morkato.bmt.context;

public interface SlashOptionBuilder {
  SlashOptionBuilder setDescription(String desc);
  SlashOptionBuilder setRequired();
  SlashOptionBuilder addChoice(String name, String value);
  void queue();
}
