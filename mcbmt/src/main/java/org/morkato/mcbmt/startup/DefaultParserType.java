package org.morkato.mcbmt.startup;

import org.morkato.mcbmt.components.ObjectParser;
import org.morkato.mcbmt.generated.registries.ObjectParserRegistry;
import org.morkato.mcbmt.commands.sharedrules.IntegerParser;
import org.morkato.mcbmt.commands.sharedrules.StringParser;
import java.util.HashMap;
import java.util.Map;

public class DefaultParserType {
  private final ObjectParser<String> STRING_OBJECT_PARSER = new StringParser();
  private final ObjectParser<Integer> INTEGER_OBJECT_PARSER = new IntegerParser();
  private final Map<Class<?>, ObjectParserRegistry<?>> parsers = new HashMap<>();
  private boolean closed = false;

  public DefaultParserType() {
    parsers.put(String.class, new ObjectParserRegistry<>(STRING_OBJECT_PARSER, String.class));
    parsers.put(Integer.class, new ObjectParserRegistry<>(INTEGER_OBJECT_PARSER, Integer.class));
  }

  public ObjectParserRegistry<?> getDefaultParser(Class<?> clazz) {
    if (closed)
      throw new IllegalStateException();
    return parsers.get(clazz);
  }

  public void close() {
    if (closed)
      return;
    parsers.clear();
    closed = true;
  }
}
