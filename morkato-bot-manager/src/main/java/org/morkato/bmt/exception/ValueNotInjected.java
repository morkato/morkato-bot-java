package org.morkato.bmt.exception;

import org.morkato.utility.exception.MorkatoUtilityException;

public class ValueNotInjected extends MorkatoUtilityException{
  private final Class<?> type;
  public ValueNotInjected(Class<?> type) {
    super("Class: " + type.getName() + " is not injected.");
    this.type = type;
  }
  public Class<?> getType() {
    return this.type;
  }
}
