package org.morkato.bmt.bmt.parser;

import org.morkato.bmt.bmt.errors.ArgumentParserException;
import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bmt.components.ObjectParser;

public class StringParser implements ObjectParser<String> {
  @Override
  public String parse(TextCommandContext<?> ignored, String text) throws ArgumentParserException {
    /* TODO: Adicionar validações por anotações. Pré-carregamento na memória pra melhor performace (Adicionar validação em build-time) */
    return text;
  }
}
