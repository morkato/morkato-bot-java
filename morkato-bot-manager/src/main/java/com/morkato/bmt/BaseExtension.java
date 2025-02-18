package com.morkato.bmt;

import com.morkato.bmt.commands.CommandManager;

public class BaseExtension implements Extension {
  @Override
  public void start(ApplicationContextBuilder application) {}
  @Override
  public void setup(CommandManager context) {}
  @Override
  public void close() {}
}
