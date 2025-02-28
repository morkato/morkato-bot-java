package org.morkato.bmt.components;

import org.morkato.bmt.ApplicationContextBuilder;
import org.morkato.bmt.context.ExtensionSetupContext;
import org.morkato.bmt.management.CommandManager;

public class BaseExtension implements Extension {
  @Override
  public void start(ApplicationContextBuilder application) throws Throwable {}
  @Override
  public void setup(ExtensionSetupContext context) throws Throwable {}
  @Override
  public void close() {}
}
