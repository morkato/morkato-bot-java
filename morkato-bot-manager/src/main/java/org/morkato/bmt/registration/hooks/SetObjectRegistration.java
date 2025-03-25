package org.morkato.bmt.registration.hooks;

import org.morkato.bmt.registration.RegisterManagement;
import org.morkato.bmt.registration.registries.Registry;

import java.util.stream.Collectors;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class SetObjectRegistration<R extends Registry<T>, T> implements RegisterManagement<R, T>{
  private final Set<R> registries = new HashSet<>();

  @Override
  public Collection<R> registries() {
    return Set.copyOf(registries);
  }

  @Override
  public Collection<T> items(){
    return registries.stream().map(Registry::getRegistry).collect(Collectors.toUnmodifiableSet());
  }

  protected void add(R registry) {
    registries.add(registry);
  }

  @Override
  public void clear() {
    registries.clear();
  }

  @Override
  public int size(){
    return registries.size();
  }
}
