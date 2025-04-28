package org.morkato.boot.exception;

public class ValueAlreadyInjected extends MorkatoBootException {
  public ValueAlreadyInjected(Class<?> clazz) {
    super("Class: " + clazz.getName() + " is already injected.");
  }
}
