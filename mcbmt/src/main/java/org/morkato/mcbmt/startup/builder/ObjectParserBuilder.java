package org.morkato.mcbmt.startup.builder;

import org.morkato.mcbmt.components.ObjectParser;
import org.morkato.mcbmt.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class ObjectParserBuilder<T> {
  private final AppBuilder.AppBuilderImpl tree;
  private final Extension<BotContext> extension;
  private final ObjectParser<T> parser;
  private boolean isQueue = false;
  public ObjectParserBuilder(
    AppBuilder.AppBuilderImpl tree,
    Extension<BotContext> extension,
    ObjectParser<T> parser
  ) {
    this.tree = Objects.requireNonNull(tree);
    this.extension = Objects.requireNonNull(extension);
    this.parser = Objects.requireNonNull(parser);
  }

  public void queue() {
    if (isQueue)
      return;
    this.tree.addObjectParser(parser);
    isQueue = true;
  }
}
