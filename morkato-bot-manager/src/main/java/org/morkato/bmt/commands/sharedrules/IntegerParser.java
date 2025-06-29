package org.morkato.bmt.commands.sharedrules;

import org.morkato.bmt.commands.CommandContext;
import org.morkato.bmt.exception.ArgumentParserException;
import org.morkato.bmt.components.ObjectParser;

public class IntegerParser implements ObjectParser<Integer> {

  @Override
  public Integer parse(CommandContext<?> ignored,String text) throws ArgumentParserException {
    /* TODO: Adicionar validações com as anotações. */
    return Integer.valueOf(text);
  }
}
