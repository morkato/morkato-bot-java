package org.morkato.bmt.context;

import org.morkato.bmt.components.ObjectGenericParser;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.utility.StringView;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Objects;

public class ObjectParserContext {
  private final TextCommandContext<?> context;
  private final Annotation[] annotations;
  private final String text;
  private final Class<?> clazz;
  private final ArgumentManager arguments;
  private final StringView view;

  public ObjectParserContext(
    @Nonnull TextCommandContext<?> context,
    @Nonnull Annotation[] annotations,
    @Nonnull String text,
    Class<?> clazz,
    @Nonnull ArgumentManager arguments
  ) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(annotations);
    Objects.requireNonNull(text);
    Objects.requireNonNull(arguments);
    this.context = context;
    this.annotations = annotations;
    this.text = text;
    this.clazz = clazz;
    this.arguments = arguments;
    this.view = new StringView(text);
  }

  @Nonnull
  public String getText() {
    return text;
  }

  @Nonnull
  public Annotation[] getAnnotations() {
    return annotations;
  }

  @Nonnull
  public TextCommandContext<?> getContext() {
    return context;
  }

  @Nonnull
  public Class<?> getParameterType() {
    Objects.requireNonNull(clazz);
    return clazz;
  }

  @Nonnull
  public ArgumentManager getArguments() {
    return arguments;
  }

  @Nonnull
  public StringView getView() {
    return view;
  }
}
