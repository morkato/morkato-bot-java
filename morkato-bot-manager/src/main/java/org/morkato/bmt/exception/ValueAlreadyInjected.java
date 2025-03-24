package org.morkato.bmt.exception;

import org.morkato.utility.exception.MorkatoUtilityException;

public class ValueAlreadyInjected extends MorkatoUtilityException{
  public ValueAlreadyInjected(Class<?> clazz) {
    super("Class: " + clazz.getName() + " is already injected.");
  }
}
