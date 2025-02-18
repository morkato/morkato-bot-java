package com.morkato.bmt.errors;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class ObjectNotFound extends InjectionException {
  public ObjectNotFound(
    @NotNull Field field
  ) {
    super("Do not injected the value for: " + field.getType().getName() + " for extension (class): " + field.getDeclaringClass().getName() + " to inject in field: " + field.getName() + ".", field);
  }
}
