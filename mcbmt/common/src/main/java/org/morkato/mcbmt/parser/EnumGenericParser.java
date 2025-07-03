package org.morkato.mcbmt.parser;

import org.morkato.mcbmt.startup.management.ReferenceGetter;
import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.components.ObjectParser;
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
