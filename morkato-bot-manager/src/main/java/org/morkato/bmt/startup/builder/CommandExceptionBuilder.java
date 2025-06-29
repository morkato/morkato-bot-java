package org.morkato.bmt.startup.builder;

import org.morkato.bmt.components.CommandExceptionHandler;
import org.morkato.bmt.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class CommandExceptionBuilder<T extends Throwable> {
  private final AppBuilder.AppBuilderImpl tree;
  private final Extension<BotContext> extension;
  private final CommandExceptionHandler<T> handler;
  private boolean isQueue = false;
  public CommandExceptionBuilder(
    AppBuilder.AppBuilderImpl tree,
    Extension<BotContext> extension,
    CommandExceptionHandler<T> exceptionhandler
  ) {
    this.tree = Objects.requireNonNull(tree);
    this.extension = Objects.requireNonNull(extension);
    this.handler = Objects.requireNonNull(exceptionhandler);
  }

  public void queue() {
    if (isQueue)
      return;
    this.tree.addCommandExceptionHandler(handler);
    isQueue = true;
  }
}
