package com.morkato.bmt.commands;

import javax.annotation.Nonnull;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ArgumentParser {
  private static Map<Class<?>, Function<StringView, ?>> builtinParsers = new HashMap<>();
  private static Map<Class<?>, Function<String, ?>> parsers = new HashMap<>();
  static {
    builtinParsers.put(StringView.class, Function.identity());
    builtinParsers.put(String.class, StringView::rest);
    parsers.put(String.class, Function.identity());
  }
  private static <T extends Record> T parseRecord(
    @Nonnull Class<T> clazz,
    @Nonnull StringView view
  ) throws Throwable {
    RecordComponent[] components = clazz.getRecordComponents();
    Object[] values = new Object[components.length];
    for (int i = 0; i < components.length; ++i) {
      RecordComponent component = components[i];
      Class<?> paramType = component.getType();
      Function<String, ?> func = parsers.get(paramType);
      if (func == null)
        throw new RuntimeException();
      String word = view.quotedWord();
      Object object = func.apply(word);
      values[i] = object;
    }
    return (T) clazz.getDeclaredConstructor(
      Arrays.stream(components)
        .map(RecordComponent::getType)
        .toArray(Class<?>[]::new)
    ).newInstance(values);
  }
  private static <T> T castOrNull(
    @Nonnull Class<T> clazz,
    @Nonnull Object object
  ) {
    try {
      return clazz.cast(object);
    } catch (ClassCastException exc) {
      return null;
    }
  }
  private Class<?>[] getParametersTypesRecord(RecordComponent[] components) {
    Class<?>[] parameters = new Class<?>[components.length];
    for (int idx = 0; idx < components.length; ++idx) {
      RecordComponent component = components[idx];
      parameters[idx] = component.getType();
    }
    return parameters;
  }
  @SuppressWarnings("unchecked")
  public <T> T parse(
    @Nonnull Class<T> clazz,
    @Nonnull StringView view
  ) {
    view.skipWhitespace();
    if (view.eof())
      return null;
    if (clazz.isRecord()) {
      try {
        return (T) this.parseRecord(clazz.asSubclass(Record.class), view);
      } catch (Throwable exc) {
        return null;
      }
    }
    Function<StringView, ?> func = builtinParsers.get(clazz);
    if (func == null)
      return null;
    Object object = func.apply(view);
    return castOrNull(clazz, object);
  }
}
