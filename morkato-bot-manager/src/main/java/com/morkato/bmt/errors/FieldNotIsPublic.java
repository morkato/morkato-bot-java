package com.morkato.bmt.errors;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class FieldNotIsPublic extends InjectionException {
  public FieldNotIsPublic(
    @NotNull Field field
  ) {
    super("Field: " + field.getName() + " is not public in extension (class): " + field.getDeclaringClass().getName(), field);
  }
}
