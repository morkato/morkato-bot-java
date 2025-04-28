package org.morkato.boot;

public interface Extension<T> {
  void start(ApplicationContext<T> application) throws Throwable;
  void setup(T context) throws Throwable;
  void close();
}
