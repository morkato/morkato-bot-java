package org.morkato.mcbmt.exception;

import org.morkato.mcbmt.exception.MorkatoBotException;

public class RecordInternalParserNotFound extends MorkatoBotException{
  public RecordInternalParserNotFound(Class<?> clazz) {
    super("Object parser for class: " + clazz.getName() + " is not found. Impossible construct Record parsing reference.");
  }
}
