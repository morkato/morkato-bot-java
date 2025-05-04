package org.morkato.bmt.generated.registries;

import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.CommandContext;
import org.morkato.bmt.registration.management.ObjectParserRegistryManagement;

public class ObjectParserRegistry<T> {
  private final ObjectParser<?> parser;
  private final Class<?> clazz;

  public ObjectParserRegistry(ObjectParser<?> parser, Class<?> clazz) {
    this.parser = parser;
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public T parse(CommandContext<?> context,String rest) throws Throwable {
    return (T)parser.parse(context, rest);
  }

  public void flush(ObjectParserRegistryManagement management) throws Throwable {
    parser.flush(management);
  }
}
