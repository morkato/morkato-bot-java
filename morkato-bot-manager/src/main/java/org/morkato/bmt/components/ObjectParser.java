package org.morkato.bmt.components;

import org.morkato.bmt.errors.ArgumentParserException;

import java.lang.annotation.Annotation;

public interface ObjectParser<T> {
  T parse(String text, Annotation[] annotations) throws ArgumentParserException;
}
