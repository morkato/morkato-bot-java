package org.morkato.mcbmt.startup.builder;

import org.morkato.mcbmt.startup.attributes.ActionAttributes;
import org.morkato.mcbmt.startup.payload.ActionPayload;
import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class ActionBuilder<T> {
  private final AppBuilder.AppBuilderImpl tree;
  private final Extension<BotContext> extension;
  private final ActionHandler<T> actionhandler;
  private String customid;
  private boolean isQueue = false;
  public ActionBuilder(
    AppBuilder.AppBuilderImpl tree,
    Extension<BotContext> extension,
    ActionHandler<T> actionhandler
  ) {
    this.tree = Objects.requireNonNull(tree);
    this.extension = Objects.requireNonNull(extension);
    this.actionhandler = Objects.requireNonNull(actionhandler);
  }

  public ActionBuilder<T> setId(String id) {
    this.customid = id;
    return this;
  }

  public void queue() {
    if (isQueue)
      return;
    this.tree.addAction(new ActionPayload(actionhandler, new ActionAttributes(customid)));
    isQueue = true;
  }
}
