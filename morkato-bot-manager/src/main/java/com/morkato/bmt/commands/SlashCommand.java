package com.morkato.bmt.commands;

public interface SlashCommand<Args> {
  void invoke(Interaction<Args> interaction) throws Throwable;
}
