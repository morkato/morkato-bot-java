package org.morkato.bmt.parsers;

import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.components.ObjectParser;

import java.lang.annotation.Annotation;

public class IntegerParser implements ObjectParser<Integer> {
  @Override
  public Integer parse(String text, Annotation[] annotations) throws ArgumentParserException {
    /* TODO: Adicionar validações com as anotações. */
    return Integer.valueOf(text);
  }
}
