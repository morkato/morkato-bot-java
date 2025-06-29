package org.morkato.bmt.components;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.commands.CommandContext;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.Map;

public interface CommandExceptionHandler<T extends Throwable> {
  @SuppressWarnings("unchecked")
  static Class<? extends Throwable> getArgument(CommandExceptionHandler<?> object) {
    Map<TypeVariable<?>,Type> typeArguments = TypeUtils.getTypeArguments(object.getClass(), CommandExceptionHandler.class);
    return (Class<? extends Throwable>)typeArguments.values().iterator().next();
  }
  void doException(CommandContext<?> context,T exception);
}
