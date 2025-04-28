package org.morkato.bmt.registration;

public interface RegisterManagement<P, T> extends RegisterInfo<T> {
  void register(P payload);
  default void flush() {}
}
