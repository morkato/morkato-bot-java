package org.morkato.bmt.components;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.argument.NoArgs;
import org.morkato.bmt.context.TextCommandContext;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Command<Args> {
  @Nonnull
  static Map<Class<? extends Command>, Class<?>> getAllCommandArgs(
    String classpath
  ) {
    /* Obtém todos os argumentos de comandos dentro de um classpath e os armazena em cache. */
    Map<Class<? extends Command>, Class<?>> classes = new HashMap<>();
    Reflections reflections = new Reflections(classpath, Scanners.SubTypes);
    Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
    for (Class<? extends Command> command : commands) {
      classes.put(command, getArgument(command));
    }
    return Collections.unmodifiableMap(classes);
  }
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
  static Class<? extends Extension> getExtension(Class<? extends Command> clazz) {
    if (clazz.isAnnotationPresent(MorkatoComponent.class))
      return null;
    return clazz.getAnnotation(MorkatoComponent.class).extension();
  }
  void invoke(TextCommandContext<Args> context) throws Throwable;
}
