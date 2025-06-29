package org.morkato.bmt.components;

import org.morkato.bmt.startup.management.ReferenceGetter;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.commands.CommandContext;
import java.lang.reflect.TypeVariable;
import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Map;

public interface ObjectParser<T> {
  @Nonnull
  static Class<?> getArgument(Class<? extends ObjectParser> clazz) {
    Map<TypeVariable<?>, Type> args = TypeUtils.getTypeArguments(clazz, ObjectParser.class);
    Type type = args.values().iterator().next();
    return (Class<?>)type;
  }

  T parse(CommandContext<?> context, String text) throws Exception;
  default void flush(ReferenceGetter registries) throws Exception {}
}
