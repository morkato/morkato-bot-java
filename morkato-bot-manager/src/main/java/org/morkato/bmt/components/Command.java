package org.morkato.bmt.components;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.argument.NoArgs;
import javax.annotation.Nonnull;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.Map;

public interface Command<Args> {
  @Nonnull
  static Class<?> getArgument(Class<? extends Command> clazz) {
    Map<TypeVariable<?>, Type> args = TypeUtils.getTypeArguments(clazz, Command.class);
    if (args.isEmpty())
      return NoArgs.class;
    Type type = args.values().iterator().next();
    boolean isClass = type instanceof Class;
    if (!isClass)
      return NoArgs.class;
    return (Class<?>) type;
  }
  void invoke(TextCommandContext<Args> context) throws Throwable;
}
