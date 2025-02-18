package com.morkato.bmt.errors;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class InjectionException extends MorkatoBotException {
  private Field field;
  public InjectionException(
    @NotNull String message,
    @NotNull Field field
  ) {
    super(message);
    this.field = field;
  }
  public Field getField() {
    return this.field;
  }
}
