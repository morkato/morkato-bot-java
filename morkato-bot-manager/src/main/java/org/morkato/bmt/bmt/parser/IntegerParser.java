package org.morkato.bmt.bmt.parser;

import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bmt.errors.ArgumentParserException;
import org.morkato.bmt.bmt.components.ObjectParser;

public class IntegerParser implements ObjectParser<Integer> {
  @Override
  public Integer parse(TextCommandContext<?> ignored, String text) throws ArgumentParserException {
    /* TODO: Adicionar validações com as anotações. */
    return Integer.valueOf(text);
  }
}
