package org.morkato.utility;

public class LambdaUtil {
  public static Runnable createAll(Runnable... runnables) {
    return () -> {
      for (Runnable runnable : runnables) {
        runnable.run();
      }
    };
  }
}
