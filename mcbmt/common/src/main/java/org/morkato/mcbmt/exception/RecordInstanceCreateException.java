package org.morkato.mcbmt.exception;

import org.morkato.mcbmt.exception.ArgumentParserException;

public class RecordInstanceCreateException extends ArgumentParserException{
  public RecordInstanceCreateException(Class<? extends Record> clazz) {
    super(null);
  }
}
