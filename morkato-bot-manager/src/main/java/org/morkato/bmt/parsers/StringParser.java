package org.morkato.bmt.parsers;

import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.components.ObjectParser;
import java.lang.annotation.Annotation;

public class StringParser implements ObjectParser<String> {
  @Override
  public String parse(TextCommandContext<?> context, String text,Annotation[] annotations) throws ArgumentParserException {
    /* TODO: Adicionar validações por anotações. Pré-carregamento na memória pra melhor performace (Adicionar validação em build-time) */
    return text;
  }
}
