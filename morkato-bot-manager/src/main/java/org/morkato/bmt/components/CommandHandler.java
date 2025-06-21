package org.morkato.bmt.components;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.context.CommandContext;
import org.morkato.bmt.NoArgs;
import javax.annotation.Nonnull;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.Map;

public interface CommandHandler<Args> {
  @Nonnull
  static Class<?> getArgument(Class<? extends CommandHandler> clazz) {
    Map<TypeVariable<?>, Type> args = TypeUtils.getTypeArguments(clazz, CommandHandler.class);
    if (args.isEmpty())
      return NoArgs.class;
    Type type = args.values().iterator().next();
    boolean isClass = type instanceof Class;
    if (!isClass)
      return NoArgs.class;
    return (Class<?>) type;
  }
  void invoke(CommandContext<Args> context) throws Exception;
}
