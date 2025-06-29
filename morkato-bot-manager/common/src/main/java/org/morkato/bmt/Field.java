package org.morkato.bmt;

import org.morkato.bmt.commands.CommandContext;
import org.morkato.bmt.components.ObjectParser;

import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;
import java.util.Objects;

public class Field<T> implements ObjectParser<T> {
  private final Annotation[] annotations;
  private ObjectParser<T> parser;
  public Field(
    @Nonnull Annotation[] annotations
  ) {
    Objects.requireNonNull(annotations);
    this.annotations = annotations;
  }

  public void setObjectParser(ObjectParser<T> parser) {
    this.parser = parser;
  }

  @SuppressWarnings("unchecked")
  public void setObjectParserAndCast(ObjectParser<?> parser) {
    this.setObjectParser((ObjectParser<T>)parser);
  }

  @Override
  public T parse(CommandContext<?> context,String text) throws Exception {
    return parser.parse(context, text);
  }

  public Annotation[] getAnnotations() {
    return annotations;
  }

  public boolean isFieldAnnotatedWith(Class<? extends Annotation> annotation) {
    for (Annotation current : annotations)
      if (annotation.isInstance(current))
        return true;
    return false;
  }

  @SuppressWarnings("unchecked")
  public <A extends Annotation> A getAnnotation(Class<A> clazz) {
    for (Annotation current : annotations)
      if (Objects.equals(clazz, current.getClass()))
        return (A)current;
    return null;
  }
}
