package org.morkato.bmt.components;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.NoArgs;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

public interface SlashCommand<Args> {
  @Nonnull
  static Class<?> getArgument(Class<? extends SlashCommand> clazz) {
    Map<TypeVariable<?>,Type> args = TypeUtils.getTypeArguments(clazz, SlashCommand.class);
    if (args.isEmpty())
      return NoArgs.class;
    Type type = args.values().iterator().next();
    boolean isClass = type instanceof Class;
    if (!isClass)
      return NoArgs.class;
    return (Class<?>) type;
  }
  void invoke(CommandInteraction interaction, Args args) throws Throwable;
}
