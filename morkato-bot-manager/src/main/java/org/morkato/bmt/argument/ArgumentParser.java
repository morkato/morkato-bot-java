package org.morkato.bmt.argument;

import org.morkato.bmt.components.ObjectGenericParser;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.utility.StringView;

import javax.annotation.Nonnull;

public class ArgumentParser {
  private static final ArgumentManager manager = ArgumentManager.get();
  static {
//    specials.put(StringView.class, Function.identity());
//    specials.put(Record.class, this::parseRecord);
//    specials.put(Enum.class, this::parseEnum);
//    parsers.put(String.class, Function.identity());
//    parsers.put(Integer.class, Integer::valueOf);
//    parsers.put(int.class, Integer::valueOf);
//    parsers.put(Long.class, Long::valueOf);
//    parsers.put(long.class, Long::valueOf);
//    parsers.put(Double.class, Double::valueOf);
//    parsers.put(double.class, Double::valueOf);
//    parsers.put(Float.class, Float::valueOf);
//    parsers.put(float.class, Float::valueOf);
//    parsers.put(Short.class, Short::valueOf);
//    parsers.put(short.class, Short::valueOf);
//    parsers.put(Boolean.class, Boolean::valueOf);
//    parsers.put(boolean.class, Boolean::valueOf);
//    parsers.put(BigDecimal.class, BigDecimal::new);
//    parsers.put(BigInteger.class, BigInteger::new);
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
  @SuppressWarnings("unchecked")
  public <T> T parse(
    @Nonnull Class<T> clazz,
    @Nonnull StringView view
  ) throws ArgumentParserException {
    view.skipWhitespace();
    if (clazz.equals(StringView.class))
      return (T) view;
    if (view.eof())
      return null;
    ObjectGenericParser<T> genericParser = manager.getGenericParser(clazz);
    if (genericParser != null) {
      return (T) genericParser.transform(clazz, manager, view);
    }
    ObjectParser<T> parser = manager.getObjectParser(clazz);
    if (parser == null)
      return null;
    return parser.parse(view.rest(), clazz.getAnnotations());
  }
}
