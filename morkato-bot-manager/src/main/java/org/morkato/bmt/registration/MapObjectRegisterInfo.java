package org.morkato.bmt.registration;

import java.util.*;

public abstract class MapObjectRegisterInfo<T> implements RegisterInfo<T> {
  private final Map<Class<?>, T> items = new HashMap<>();

  protected void add(Class<?> identifier, T registry) {
    items.put(identifier, registry);
  }

  @Override
  public Collection<T> registries() {
    return items.values();
  }

  @Override
  public int size() {
    return items.size();
  }

  protected T getRegistry(Class<?> identifier) {
    return items.get(identifier);
  }
}
