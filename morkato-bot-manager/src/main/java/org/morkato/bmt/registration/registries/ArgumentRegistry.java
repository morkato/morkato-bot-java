package org.morkato.bmt.registration.registries;

import org.morkato.bmt.components.ObjectParser;

public class ArgumentRegistry implements Registry<ObjectParser<?>> {
  private final ObjectParser<?> parser;
  private final Class<?> clazz;

  public ArgumentRegistry(ObjectParser<?> parser, Class<?> clazz) {
    this.parser = parser;
    this.clazz = clazz;
  }

  @Override
  public ObjectParser<?> getRegistry() {
    return parser;
  }
}
