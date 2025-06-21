package org.morkato.bmt.internal.startup.builder;

import org.morkato.bmt.internal.startup.AppCommandTreeInternal;
import org.morkato.bmt.startup.builder.CommandExceptionBuilder;
import org.morkato.bmt.components.CommandExceptionHandler;
import org.morkato.bmt.context.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class CommandExceptionBuilderInternal<T extends Throwable> implements CommandExceptionBuilder<T> {
  private final AppCommandTreeInternal tree;
  private final Extension<BotContext> extension;
  private final CommandExceptionHandler<T> handler;
  private boolean isQueue = false;
  public CommandExceptionBuilderInternal(AppCommandTreeInternal tree, Extension<BotContext> extension, CommandExceptionHandler<T> exceptionhandler) {
    this.tree = Objects.requireNonNull(tree);
    this.extension = Objects.requireNonNull(extension);
    this.handler = Objects.requireNonNull(exceptionhandler);
  }

  @Override
  public void queue() {
    if (isQueue)
      return;
    this.tree.addCommandExceptionHandler(handler);
    isQueue = true;
  }
}
