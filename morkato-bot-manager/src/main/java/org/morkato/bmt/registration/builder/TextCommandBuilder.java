package org.morkato.bmt.registration.builder;

import org.morkato.bmt.components.Command;

public interface TextCommandBuilder<T> {
  TextCommandBuilder<T> setName(String name);
  TextCommandBuilder<T> setDescription(String desc);
  TextCommandBuilder<T> addAlias(String alias);
  TextCommandBuilder<T> commandAlias(String alias);
  <S> TextCommandBuilder<T> subcommand(String name, Command<S> subcommand);
  void queue();

}
