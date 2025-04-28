package org.morkato.bmt.registration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class SetObjectRegisterInfo<T> implements RegisterInfo<T> {
  private final Set<T> items = new HashSet<>();

  protected void add(T registry) {
    items.add(registry);
  }

  @Override
  public Collection<T> registries() {
    return Set.copyOf(items);
  }

  @Override
  public int size() {
    return items.size();
  }
}
