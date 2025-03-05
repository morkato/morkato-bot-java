package org.morkato.bmt.parser;

import org.morkato.bmt.annotation.RestText;
import org.morkato.bmt.components.ObjectGenericParser;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.ObjectParserContext;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.RecordInstanceCreateException;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.utility.StringView;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Objects;

public class RecordGenericParser extends ObjectGenericParser<Record> {
  public RecordGenericParser() {
    super(Record.class);
  }

  @Override
  public Record transform(ObjectParserContext objctx, Class<? extends Record> clazz) throws Throwable {
    TextCommandContext<?> ctx = objctx.getContext();
    StringView view = objctx.getView();
    ArgumentManager manager = objctx.getArguments();
    RecordComponent[] components = clazz.getRecordComponents();
    Object[] values = new Object[components.length];
    for (int i = 0; i < components.length; ++i) {
      RecordComponent component = components[i];
      Class<?> paramType = component.getType();
      ObjectParser<?> parser = manager.getObjectParser(paramType);
      Objects.requireNonNull(parser); /* Impossible scenery, but is prevent */
      view.skipWhitespace();
      String text = (Objects.equals(i, components.length - 1) && component.isAnnotationPresent(RestText.class))
        ? view.rest()
        : view.quotedWord();
      ObjectParserContext other = new ObjectParserContext(ctx, component.getAnnotations(), text, null, manager);
      Object any = parser.parse(other);
      values[i] = any;
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
