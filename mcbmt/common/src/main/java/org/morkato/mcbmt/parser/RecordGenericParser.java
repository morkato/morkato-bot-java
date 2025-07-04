package org.morkato.mcbmt.parser;

import org.morkato.mcbmt.startup.management.ReferenceGetter;
import org.morkato.boot.annotation.NotRequired;
import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.components.ObjectParser;
import org.morkato.mcbmt.Field;
import org.morkato.mcbmt.exception.RecordInternalParserNotFound;
import org.morkato.mcbmt.util.StringView;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.Arrays;

public class RecordGenericParser<T extends Record> implements ObjectParser<T> {
  private final Constructor<T> constructor;
  private final Field<?>[] fields;
  public RecordGenericParser(Class<T> clazz) throws NoSuchMethodException {
    final RecordComponent[] components = clazz.getRecordComponents();
    this.fields = new Field[components.length];
    Class<?>[] classes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
    this.constructor = clazz.getDeclaredConstructor(classes);
    for (int i = 0; i < fields.length; ++i) {
      RecordComponent component = components[i];
      fields[i] = new Field<>(component.getAnnotations());
    }
  }

  @Override
  public void flush(ReferenceGetter references) throws RecordInternalParserNotFound {
    /* Pré-carrega os parsers. Garantindo que todos estejam disponíveis para execução. */
    Class<?>[] parameters = constructor.getParameterTypes();
    for (int i = 0; i < parameters.length; ++i) {
      final Class<?> parameter = parameters[i];
      final ObjectParser<?> parser = references.importref(parameter);
      if (Objects.isNull(parser))
        /* TODO: Adicionar um erro customizado. */
        throw new RecordInternalParserNotFound(parameter);
      final Field<?> field = fields[i];
      field.setObjectParserAndCast(parser);
    }
  }

  @Override
  public T parse(CommandContext<?> context,String text) throws Exception {
    final StringView view = new StringView(text);
    final Object[] values = new Object[fields.length];
    for (int i = 0; i < values.length; ++i) {
      final Field<?> field = fields[i];
      view.skipWhitespace();
      if (view.eof()) {
        if (!field.isFieldAnnotatedWith(NotRequired.class))
          /* TODO: Add custom error for this scenery */
          throw new RuntimeException("StringView::eof and this field is required!");
        continue;
      }
      final String query = view.quotedWord();
      final Object value = field.parse(context, query);
      if (Objects.isNull(value)) {
        if (field.isFieldAnnotatedWith(NotRequired.class)) {
          view.undo();
          continue;
        }
        /* TODO: Add custom error for this scenery */
        throw new RuntimeException("Value is invalid, and this field is required!");
      }
      values[i] = value;
    }
    return constructor.newInstance(values);
  }
}
