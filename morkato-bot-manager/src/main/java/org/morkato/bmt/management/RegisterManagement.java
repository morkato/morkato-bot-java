package org.morkato.bmt.management;

public interface RegisterManagement<T> {
  static <T> void registerAll(RegisterManagement<T> management, Iterable<T> objects) {
    for (T obj : objects)
      management.register(obj);
  }
  @SafeVarargs
  static <T> void registerAll(RegisterManagement<T> management, T... objects) {
    for (T obj : objects)
      management.register(obj);
  }
  void register(T registry);
}
