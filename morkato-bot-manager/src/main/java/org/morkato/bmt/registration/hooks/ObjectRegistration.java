package org.morkato.bmt.registration.hooks;

import org.morkato.bmt.registration.RegisterManagement;
import org.morkato.bmt.registration.registries.Registry;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ObjectRegistration<R extends Registry<T>, T> implements RegisterManagement<R, T>{
  private final Map<Class<?>, R> registries = new HashMap<>();

  protected void add(Class<?> identifier, R registry) {
    this.registries.put(identifier, registry);
  }

  @Override
  public Collection<T> items() {
    return registries.values().stream().map(Registry::getRegistry).collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Collection<R> registries() {
    return registries.values();
  }

  @Override
  public void clear() {
    registries.clear();
  }

  @Override
  public int size() {
    return registries.size();
  }

  public R getRegistry(Class<?> clazz) {
    return registries.get(clazz);
  }
}
