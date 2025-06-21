package org.morkato.bmt.startup.builder;

import org.morkato.bmt.components.CommandHandler;

public interface TextCommandBuilder<T> {
  TextCommandBuilder<T> setName(String name);
  TextCommandBuilder<T> setDescription(String desc);
  TextCommandBuilder<T> addAlias(String alias);
  TextCommandBuilder<T> commandAlias(String alias);
  <S> TextCommandBuilder<T> subcommand(String name, CommandHandler<S> subcommand);
  void queue();
}
