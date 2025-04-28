package org.morkato.boot.exception;

public class ValueNotInjected extends MorkatoBootException{
  private final Class<?> type;
  public ValueNotInjected(Class<?> type) {
    super("Class: " + type.getName() + " is not injected.");
    this.type = type;
  }
  public Class<?> getType() {
    return this.type;
  }
}
