package org.morkato.bmt.components;

import org.morkato.bmt.ApplicationContextBuilder;
import org.morkato.bmt.context.ExtensionSetupContext;
import org.morkato.bmt.management.CommandManager;

public interface Extension {
  void start(ApplicationContextBuilder application) throws Throwable;
  void setup(ExtensionSetupContext commands) throws Throwable;
  void close();
}