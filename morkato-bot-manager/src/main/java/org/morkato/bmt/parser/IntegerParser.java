package org.morkato.bmt.parser;

import org.morkato.bmt.context.ObjectParserContext;
import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.components.ObjectParser;

public class IntegerParser implements ObjectParser<Integer> {
  @Override
  public Integer parse(ObjectParserContext context) throws ArgumentParserException {
    /* TODO: Adicionar validações com as anotações. */
    return Integer.valueOf(context.getText());
  }
}
