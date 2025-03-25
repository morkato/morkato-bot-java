package org.morkato.bmt.registration.registries;

import org.morkato.bmt.DependenceInjection;

public interface Registry<T> {
  T getRegistry();

  default void write(DependenceInjection injector) throws Throwable {
    injector.writeProperties(this.getRegistry());
    injector.write(this.getRegistry());
  }
}
