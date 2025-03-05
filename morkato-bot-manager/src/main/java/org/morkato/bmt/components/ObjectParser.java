package org.morkato.bmt.components;

import org.morkato.bmt.context.ObjectParserContext;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.ArgumentParserException;

import java.lang.annotation.Annotation;

public interface ObjectParser<T> {
  T parse(ObjectParserContext context) throws Throwable;
}
