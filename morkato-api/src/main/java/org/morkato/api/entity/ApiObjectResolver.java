package org.morkato.api.entity;

public interface ApiObjectResolver<T extends ApiObject> extends ObjectResolver<T> {
  T getByName(String name);
}
