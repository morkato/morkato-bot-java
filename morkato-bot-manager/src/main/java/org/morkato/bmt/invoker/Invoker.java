package org.morkato.bmt.invoker;

public interface Invoker<Context> {
  boolean isReady();
  void invoke(Context context);
}
