package com.morkato.bmt.commands;

public interface Command<Args> {
  void invoke(Context<Args> context) throws Throwable;
}
