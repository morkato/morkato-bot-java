package org.morkato.bmt.parser;

import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.exception.ArgumentParserException;
import org.morkato.bmt.components.ObjectParser;

public class IntegerParser implements ObjectParser<Integer> {
  @Override
  public Integer parse(TextCommandContext<?> ignored, String text) throws ArgumentParserException {
    /* TODO: Adicionar validações com as anotações. */
    return Integer.valueOf(text);
  }
}
