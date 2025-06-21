package org.morkato.bmt.generated.registries;

import org.morkato.bmt.startup.management.ReferenceGetter;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.CommandContext;

public class ObjectParserRegistry<T> {
  private final ObjectParser<?> parser;
  private final Class<?> clazz;

  public ObjectParserRegistry(ObjectParser<?> parser, Class<?> clazz) {
    this.parser = parser;
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public T parse(CommandContext<?> context,String rest) throws Exception {
    return (T)parser.parse(context, rest);
  }

  public void flush(ReferenceGetter references) throws Exception {
    parser.flush(references);
  }
}
