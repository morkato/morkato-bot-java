package org.morkato.bmt.commands.sharedrules;

import org.morkato.bmt.exception.ArgumentParserException;
import org.morkato.bmt.commands.CommandContext;
import org.morkato.bmt.components.ObjectParser;

public class StringParser implements ObjectParser<String> {
  @Override
  public String parse(CommandContext<?> ignored,String text) throws ArgumentParserException {
    /* TODO: Adicionar validações por anotações. Pré-carregamento na memória pra melhor performace (Adicionar validação em build-time) */
    return text;
  }
}
