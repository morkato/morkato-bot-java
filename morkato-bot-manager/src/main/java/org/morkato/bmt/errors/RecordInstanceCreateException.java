package org.morkato.bmt.errors;

public class RecordInstanceCreateException extends ArgumentParserException {
  public RecordInstanceCreateException(Class<? extends Record> clazz) {
    super(null);
  }
}
