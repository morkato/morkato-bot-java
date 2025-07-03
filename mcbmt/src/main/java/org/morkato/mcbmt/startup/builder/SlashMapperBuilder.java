package org.morkato.mcbmt.startup.builder;

import org.morkato.mcbmt.components.SlashMapper;
import org.morkato.mcbmt.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class SlashMapperBuilder<T> {
  private final AppBuilder.AppBuilderImpl tree;
  private final Extension<BotContext> extension;
  private final SlashMapper<T> slashmapper;
  private boolean isQueue = false;

  public SlashMapperBuilder(
    AppBuilder.AppBuilderImpl tree,
    Extension<BotContext> extension,
    SlashMapper<T> slashmapper
  ) {
    this.tree = Objects.requireNonNull(tree);
    this.extension = Objects.requireNonNull(extension);
    this.slashmapper = Objects.requireNonNull(slashmapper);
  }

  public void queue() {
    if (isQueue)
      return;
    tree.addSlashMapper(slashmapper);
    isQueue = true;
  }
}
