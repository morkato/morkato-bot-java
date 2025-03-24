package org.morkato.bmt.components;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.context.TextCommandContext;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

public interface CommandException<T extends Throwable> {
  @SuppressWarnings("unchecked")
  static Class<? extends Throwable> getExceptionClass(CommandException<?> object) {
    Map<TypeVariable<?>,Type> typeArguments = TypeUtils.getTypeArguments(object.getClass(), CommandException.class);
    return (Class<? extends Throwable>)typeArguments.values().iterator().next();
  }
  void doException(TextCommandContext<?> context,T exception);
}
