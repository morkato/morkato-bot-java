package org.morkato.bmt.components;

import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.utility.StringView;

public interface ObjectGenericParser<T> {
  Object transform(Class<? extends T> clazz, ArgumentManager parser, StringView text) throws ArgumentParserException;
}
