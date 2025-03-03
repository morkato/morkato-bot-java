package org.morkato.bmt.parsers;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.annotation.RestText;
import org.morkato.bmt.components.ObjectGenericParser;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bmt.errors.ObjectParserNotFoundException;
import org.morkato.bmt.errors.RecordInstanceCreateException;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.utility.StringView;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;

@MorkatoComponent
public class RecordGenericParser implements ObjectGenericParser<Record> {
  private <T> Object parseGeneric(Class<? extends T> clazz, TextCommandContext<?> ctx, ArgumentManager parser, StringView view) throws ObjectParserNotFoundException {
    ObjectGenericParser<T> genericParser = parser.getGenericParser(clazz);
    if (genericParser == null)
      /* Stupid impossible error. */
      throw new ObjectParserNotFoundException(clazz);
    return genericParser.transform(clazz, ctx, parser, view);
  }
  @Override
  public Object transform(Class<? extends Record> clazz, TextCommandContext<?> ctx, ArgumentManager manager, StringView view) throws ArgumentParserException {
    RecordComponent[] components = clazz.getRecordComponents();
    Object[] values = new Object[components.length];
    for (int i = 0; i < components.length; ++i) {
      RecordComponent component = components[i];
      Class<?> paramType = component.getType();
      ObjectParser<?> parser = manager.getObjectParser(paramType);
      Object object;
      if (parser == null)
        object = this.parseGeneric(paramType, ctx, manager, view);
      else {
        System.out.println(component.isAnnotationPresent(RestText.class));
        String text = (i == (components.length - 1) && component.isAnnotationPresent(RestText.class)) ? view.rest() : view.quotedWord();
        object = parser.parse(ctx, text, component.getAnnotations());
      }
      values[i] = object;
    }
    try {
      return clazz.getDeclaredConstructor(
        Arrays.stream(components)
          .map(RecordComponent::getType)
          .toArray(Class<?>[]::new)
      ).newInstance(values);
    } catch (Exception exc) {
      /* Again, a stupid impossible error (supposedly). */
      throw new RecordInstanceCreateException(clazz);
    }
  }
}
