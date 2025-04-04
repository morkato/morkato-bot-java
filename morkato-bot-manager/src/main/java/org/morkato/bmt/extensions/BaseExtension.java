package org.morkato.bmt.extensions;

import org.morkato.bmt.context.ApplicationContextBuilder;
import org.morkato.bmt.context.ExtensionSetupContext;

public class BaseExtension implements Extension{
  @Override
  public void start(ApplicationContextBuilder application) throws Throwable {}
  @Override
  public void setup(ExtensionSetupContext context) throws Throwable {}
  @Override
  public void close() {}
}
