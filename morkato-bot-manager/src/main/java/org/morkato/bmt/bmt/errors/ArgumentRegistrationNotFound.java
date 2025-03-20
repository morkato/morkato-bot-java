package org.morkato.bmt.bmt.errors;

public class ArgumentRegistrationNotFound extends MorkatoBotException {
  public ArgumentRegistrationNotFound(Class<?> clazz) {
    super("Class: " + clazz.getName() + " is not registered.");
  }
}
