package org.morkato.api.entity;

public interface ObjectResolver<T extends ObjectId>
  extends Iterable<T> {
  boolean isEmpty();
  int size();
  T[] order();
  boolean loaded();
  void resolve();
  T get(String id);
  void add(T obj);
  T remove(ObjectId obj);
}
