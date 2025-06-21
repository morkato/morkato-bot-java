package org.morkato.bmt.startup.management;

import org.morkato.boot.DependenceInjection;

import java.util.Objects;

public class StartupManagement {
  private final DependenceInjection injector;

  public StartupManagement(DependenceInjection injector) {
    this.injector =Objects.requireNonNull(injector);
  }

  protected void writeInRegistry(Object obj) throws Exception {
    injector.writeProperties(obj);
    injector.write(obj);
  }
}
