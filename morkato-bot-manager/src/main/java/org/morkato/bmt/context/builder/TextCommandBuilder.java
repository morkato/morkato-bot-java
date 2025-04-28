package org.morkato.bmt.context.builder;

public interface TextCommandBuilder<T> {
  TextCommandBuilder<T> addName(String name);
  void queue();
}
