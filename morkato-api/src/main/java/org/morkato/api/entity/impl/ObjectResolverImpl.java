package org.morkato.api.entity.impl;

import org.morkato.api.entity.ObjectId;
import org.morkato.api.entity.ObjectResolver;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ObjectResolverImpl<T extends ObjectId> implements ObjectResolver<T> {
  private final Map<String, T> items = new HashMap<>();
  private volatile boolean isLoaded = false;
  private int length = 0;
  protected abstract void resolveImpl();

  public void clean() {
    items.clear();
    length = 0;
    isLoaded = false;
  }

  @Override
  public int size() {
    return length;
  }

  @Override
  @SuppressWarnings("uncheked")
  public T[] order() {
    return null;
  }

  @Override
  public boolean loaded() {
    return isLoaded;
  }

  @Override
  public synchronized void resolve() {
    try {
      if (isLoaded) {
        return;
      }
      this.isLoaded = true;
      this.resolveImpl();
    } catch (RuntimeException exc) {
      this.clean();
      throw exc;
    }
  }

  @Override
  public T get(String id) {
    if (!isLoaded)
      return null;
    return items.get(id);
  }

  @Override
  public synchronized void add(T obj) {
    if (!isLoaded)
      return;
    this.length += 1;
    items.put(obj.getId(), obj);
  }

  @Override
  public synchronized T remove(ObjectId obj) {
    if (!isLoaded)
      return null;
    this.length -= 1;
    return items.remove(obj.getId());
  }

  @Override
  @Nonnull
  public Iterator<T> iterator() {
    return items.values().iterator();
  }
}
