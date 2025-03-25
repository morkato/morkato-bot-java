package org.morkato.bmt.registration;

import org.morkato.bmt.registration.registries.Registry;

import java.util.Collection;
import java.util.Objects;

public interface RegisterManagement<R extends Registry<T>, T> {
  static <R extends Registry<T>, T> void registerAll(RegisterManagement<R, T> management, Iterable<T> objects) {
    if (Objects.isNull(management))
      return;
    for (T obj : objects)
      management.register(obj);
  }
  Collection<R> registries();
  Collection<T> items();
  void register(T registry);
  void clear();
  int size();

  default void flush() {}
}
