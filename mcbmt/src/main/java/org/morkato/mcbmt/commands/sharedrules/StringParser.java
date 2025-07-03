package org.morkato.mcbmt.commands.sharedrules;

import org.morkato.mcbmt.exception.ArgumentParserException;
import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.components.ObjectParser;

public class StringParser implements ObjectParser<String> {
  @Override
  public String parse(CommandContext<?> ignored,String text) throws ArgumentParserException {
    /* TODO: Adicionar validações por anotações. Pré-carregamento na memória pra melhor performace (Adicionar validação em build-time) */
    return text;
  }
}
