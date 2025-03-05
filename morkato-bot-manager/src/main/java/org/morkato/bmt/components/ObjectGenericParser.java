package org.morkato.bmt.components;

import org.morkato.bmt.context.ObjectParserContext;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.utility.StringView;

public abstract class ObjectGenericParser<T> implements ObjectParser<T> {
  protected final Class<? extends T> superclazz;
  public ObjectGenericParser(Class<T> superclazz) {
    this.superclazz = superclazz;
  }
  public abstract T transform(ObjectParserContext context, Class<? extends T> clazz) throws Throwable;
  public T parse(ObjectParserContext context) throws Throwable {
    Class<?> parameterType = context.getParameterType();
    if (!superclazz.isAssignableFrom(parameterType))
      /* TODO: Add a custom error for this impossible scenery, for prevent. */
      throw new RuntimeException("ObjectGenericParser error");
    return this.transform(context, parameterType.asSubclass(superclazz));
  }
}
