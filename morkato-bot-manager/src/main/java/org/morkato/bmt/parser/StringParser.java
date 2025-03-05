package org.morkato.bmt.parser;

import org.morkato.bmt.context.ObjectParserContext;
import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.components.ObjectParser;

public class StringParser implements ObjectParser<String> {
  @Override
  public String parse(ObjectParserContext context) throws ArgumentParserException {
    /* TODO: Adicionar validações por anotações. Pré-carregamento na memória pra melhor performace (Adicionar validação em build-time) */
    return context.getText();
  }
}
