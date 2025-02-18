package com.morkato.bmt.errors;

public class ExtensionClassIsAbstract extends MorkatoBotException {
  public ExtensionClassIsAbstract(Class<?> clazz) {
    super("Class: " + clazz.getName() + " is abstract.");
  }
}
