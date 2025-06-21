package org.morkato.bmt.internal.context;

import org.morkato.bmt.internal.startup.AppCommandTreeInternal;
import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.startup.AppCommandTree;
import org.morkato.boot.Extension;

public class BotContextInternal implements BotContext {
  private final Extension<BotContext> extension;
  private final AppCommandTree tree;

  public BotContextInternal(Extension<BotContext> extension) {
    this.extension = extension;
    this.tree = new AppCommandTreeInternal(extension);
  }

  @Override
  public AppCommandTree getAppCommandsTree() {
    return tree;
  }
}
