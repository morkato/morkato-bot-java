package org.morkato.bmt.exception;

public class ExtensionClassIsAbstract extends MorkatoBotException {
  public ExtensionClassIsAbstract(Class<?> clazz) {
    super("Class: " + clazz.getName() + " is abstract.");
  }
}
