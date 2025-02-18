package com.morkato.bmt;

import com.morkato.bmt.commands.CommandManager;

public interface Extension {
  void start(ApplicationContextBuilder application);
  void setup(CommandManager commands);
  void close();
}