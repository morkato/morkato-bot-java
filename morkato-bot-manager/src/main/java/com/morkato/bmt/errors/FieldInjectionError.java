package com.morkato.bmt.errors;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class FieldInjectionError extends InjectionException {
  public FieldInjectionError(
    @NotNull Field field
  ) {
    super("An unexpected error occurred on injecting value in field: " + field.getName() + ".", field);
  }
}
