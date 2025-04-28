package org.morkato.bmt.registration;

import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.registration.management.ObjectParserRegistryManagement;

public class ObjectParserRegistry<T> {
  private final ObjectParser<?> parser;
  private final Class<?> clazz;

  public ObjectParserRegistry(ObjectParser<?> parser, Class<?> clazz) {
    this.parser = parser;
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public T parse(TextCommandContext<?> context,String rest) throws Throwable {
    return (T)parser.parse(context, rest);
  }

  public void flush(ObjectParserRegistryManagement management) throws Throwable {
    parser.flush(management);
  }
}
