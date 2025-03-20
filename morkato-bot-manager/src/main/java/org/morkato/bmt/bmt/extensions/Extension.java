package org.morkato.bmt.bmt.extensions;

import org.morkato.bmt.bmt.context.ApplicationContextBuilder;
import org.morkato.bmt.bmt.context.ExtensionSetupContext;

public interface Extension {
  void start(ApplicationContextBuilder application) throws Throwable;
  void setup(ExtensionSetupContext commands) throws Throwable;
  void close();
}