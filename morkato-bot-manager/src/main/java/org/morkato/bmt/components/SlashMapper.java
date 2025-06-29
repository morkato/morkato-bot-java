package org.morkato.bmt.components;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.commands.rules.SlashMappingInteraction;
import org.morkato.bmt.commands.rules.SlashMapperData;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.Map;

public interface SlashMapper<T> {
  static Class<?> getArgument(Class<? extends SlashMapper> clazz) {
    Map<TypeVariable<?>,Type> args = TypeUtils.getTypeArguments(clazz, SlashMapper.class);
    Type type = args.values().iterator().next();
    boolean isClass = type instanceof Class;
    if (!isClass)
      throw new RuntimeException("Impossible stupid error.");
    return (Class<?>)type;
  }
  void createOptions(SlashMappingInteraction interaction);
  T mapInteraction(SlashMapperData payload);
}
