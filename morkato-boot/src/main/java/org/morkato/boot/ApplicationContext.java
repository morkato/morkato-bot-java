package org.morkato.boot;

public interface ApplicationContext<T> {
  Extension<T> getRunningExtension();
  <P> void inject(P value);
}
