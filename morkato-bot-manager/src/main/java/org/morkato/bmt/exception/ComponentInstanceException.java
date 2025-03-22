package org.morkato.bmt.exception;

public class ComponentInstanceException extends MorkatoBotException {
  public ComponentInstanceException(Class<?> clazz, Exception exc) {
    super("Error to instance class: " + clazz.getName() + " an error occurred: " + exc.getClass().getName());
  }
}
