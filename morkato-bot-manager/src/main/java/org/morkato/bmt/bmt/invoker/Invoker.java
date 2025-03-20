package org.morkato.bmt.bmt.invoker;

import org.morkato.bmt.bmt.registration.impl.MorkatoBotManagerRegistration;

public interface Invoker<Context> {
  static CommandInvoker createDefaultCommandInvoker(MorkatoBotManagerRegistration registration) {
    return new CommandInvoker(registration);
  }
  boolean isReady();
  void invoke(Context context);
}
