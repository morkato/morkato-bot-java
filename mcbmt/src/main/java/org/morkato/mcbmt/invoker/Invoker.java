package org.morkato.mcbmt.invoker;

import org.morkato.mcbmt.BotCore;

public interface Invoker<Context> {
  boolean isReady();
  void invoke(Context context);
  void start(BotCore core);
  void shutdown();
}
