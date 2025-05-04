package org.morkato.bmt.internal.registration.builder;

import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.internal.registration.AppCommandTreeInternal;
import org.morkato.bmt.registration.builder.ObjectParserBuilder;
import org.morkato.bmt.context.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class ObjectParserBuilderInternal<T> implements ObjectParserBuilder<T> {
  private final AppCommandTreeInternal tree;
  private final Extension<BotContext> extension;
  private final ObjectParser<T> parser;
  private boolean isQueue = false;
  public ObjectParserBuilderInternal(AppCommandTreeInternal tree, Extension<BotContext> extension, ObjectParser<T> parser) {
    this.tree = Objects.requireNonNull(tree);
    this.extension = Objects.requireNonNull(extension);
    this.parser = Objects.requireNonNull(parser);
  }

  @Override
  public void queue() {
    if (isQueue)
      return;
    this.tree.addObjectParser(parser);
    isQueue = true;
  }
}
