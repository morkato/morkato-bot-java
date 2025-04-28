package org.morkato.boot;

public interface ExtensionContextFactory<T> {
  T make(Extension<T> extension);
  void commit(Extension<T> extension, T context);
}
