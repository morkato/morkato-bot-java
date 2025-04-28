package org.morkato.boot;

public class BaseExtension<T> implements Extension<T> {
  @Override
  public void start(ApplicationContext<T> application) throws Throwable {}
  @Override
  public void setup(T context) throws Throwable {}
  @Override
  public void close() {}
}
