package org.morkato.mcbmt.commands.sharedrules;

import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.exception.ArgumentParserException;
import org.morkato.mcbmt.components.ObjectParser;

public class IntegerParser implements ObjectParser<Integer> {

  @Override
  public Integer parse(CommandContext<?> ignored,String text) throws ArgumentParserException {
    /* TODO: Adicionar validações com as anotações. */
    return Integer.valueOf(text);
  }
}
