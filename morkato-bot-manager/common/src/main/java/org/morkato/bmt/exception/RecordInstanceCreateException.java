package org.morkato.bmt.exception;

public class RecordInstanceCreateException extends ArgumentParserException {
  public RecordInstanceCreateException(Class<? extends Record> clazz) {
    super(null);
  }
}
