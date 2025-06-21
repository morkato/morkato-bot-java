package org.morkato.bmt.parser;

import org.morkato.bmt.startup.management.ReferenceGetter;
import org.morkato.bmt.context.CommandContext;
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
  public void flush(ReferenceGetter references) {
  }

  @Override
  @SuppressWarnings("unchecked")
  public T parse(CommandContext<?> context, String text) {
    return (T)values.get(text.toUpperCase());
  }
}
