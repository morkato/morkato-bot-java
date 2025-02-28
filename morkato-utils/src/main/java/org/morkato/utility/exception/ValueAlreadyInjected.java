package org.morkato.utility.exception;

public class ValueAlreadyInjected extends MorkatoUtilityException {
  public ValueAlreadyInjected(Class<?> clazz) {
    super("Class: " + clazz.getName() + " is already injected.");
  }
}
