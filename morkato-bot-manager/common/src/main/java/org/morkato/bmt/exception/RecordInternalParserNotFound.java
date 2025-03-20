package org.morkato.bmt.exception;

public class RecordInternalParserNotFound extends MorkatoCommonException {
  public RecordInternalParserNotFound(Class<?> clazz) {
    super("Object parser for class: " + clazz.getName() + " is not found. Impossible construct Record parsing reference.");
  }
}
