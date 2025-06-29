package org.morkato.bmt;

import org.morkato.bmt.startup.builder.AppBuilder;
import org.morkato.boot.Extension;

public class BotContextImpl implements BotContext {
  private final Extension<BotContext> extension;
  private final AppBuilder tree;

  public BotContextImpl(Extension<BotContext> extension) {
    this.extension = extension;
    this.tree = new AppBuilder.AppBuilderImpl(extension);
  }

  @Override
  public AppBuilder getAppCommandsTree() {
    return tree;
  }
}
