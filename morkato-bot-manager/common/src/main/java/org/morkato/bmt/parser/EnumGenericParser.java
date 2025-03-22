package org.morkato.bmt.parser;

import org.morkato.bmt.registration.ArgumentRegistration;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.ObjectParser;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class EnumGenericParser<T extends Enum<T>> implements ObjectParser<T> {
  private final Map<String, Enum<T>> values = new HashMap<>();

  public EnumGenericParser(Class<T> clazz) {
    for (Enum<T> value : EnumSet.allOf(clazz)) {
      values.put(value.name(), value);
    }
  }

  @Override
  public void flush(ArgumentRegistration registration) {
  }

  @Override
  @SuppressWarnings("unchecked")
  public T parse(TextCommandContext<?> context, String text) {
    return (T)values.get(text.toUpperCase());
  }
}
