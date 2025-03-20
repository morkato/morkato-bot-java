package org.morkato.bmt.bmt.registration;

import org.morkato.bmt.bmt.management.RegisterManagement;

import java.util.HashSet;
import java.util.Set;

public class RecordsRegistrationProxy<T, O extends RegisterManagement<T>> implements RegisterManagement<T> {
  private final O management;
  private final Set<T> records = new HashSet<>();

  public RecordsRegistrationProxy(O management) {
    this.management = management;
  }

  public O getOriginal() {
    return management;
  }

  @Override
  public void register(T registry) {
    records.add(registry);
  }

  @Override
  public void clear() {
    records.clear();
    management.clear();
  }

  public void flush() {
    RegisterManagement.registerAll(management, records);
    management.flush();
    records.clear();
  }
}
