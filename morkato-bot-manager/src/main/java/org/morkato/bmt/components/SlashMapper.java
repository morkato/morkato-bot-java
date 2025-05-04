package org.morkato.bmt.components;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
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
  Collection<OptionData> createOptions();
  T mapInteraction(CommandInteraction interaction);
}
