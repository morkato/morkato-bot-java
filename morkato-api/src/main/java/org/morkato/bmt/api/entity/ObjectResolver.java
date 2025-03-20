package org.morkato.bmt.api.entity;

public interface ObjectResolver<T extends ObjectId>
  extends Iterable<T> {
  int size();
  T[] order();
  boolean loaded();
  void resolve();
  T get(String id);
  void add(T obj);
  T remove(ObjectId obj);
}
