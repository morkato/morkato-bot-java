package org.morkato.bmt.registration;

import java.util.Objects;

public interface RegisterManagement<T> {
  static <T> void registerAll(RegisterManagement<T> management, Iterable<T> objects) {
    if (Objects.isNull(management))
      return;
    for (T obj : objects)
      management.register(obj);
  }
  default void flush() {}
  void register(T registry);
  void clear();
}
