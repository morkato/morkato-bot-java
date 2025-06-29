package org.morkato.bmt.invoker;

import org.morkato.bmt.BotCore;

public interface Invoker<Context> {
  boolean isReady();
  void invoke(Context context);
  void start(BotCore core);
  void shutdown();
}
