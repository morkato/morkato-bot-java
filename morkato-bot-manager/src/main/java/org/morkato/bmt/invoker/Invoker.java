package org.morkato.bmt.invoker;

import org.morkato.bmt.registration.MorkatoBotManagerRegistration;

public interface Invoker<Context> {
//  static CommandInvoker createDefaultCommandInvoker(MorkatoBotManagerRegistration registration) {
//    return new CommandInvoker(registration);
//  }
  boolean isReady();
  void invoke(Context context);
}
