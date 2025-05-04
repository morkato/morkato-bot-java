package org.morkato.bmt.internal.registration.builder;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.internal.registration.AppCommandTreeInternal;
import org.morkato.bmt.registration.builder.SlashMapperBuilder;
import org.morkato.boot.Extension;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SlashMapperBuilderInternal<T> implements SlashMapperBuilder<T> {
  private final AppCommandTreeInternal tree;
  private final Extension<BotContext> extension;
  private final SlashMapper<T> slashmapper;
  private boolean isQueue = false;

  public SlashMapperBuilderInternal(AppCommandTreeInternal tree, Extension<BotContext> extension, SlashMapper<T> slashmapper) {
    this.tree = Objects.requireNonNull(tree);
    this.extension = Objects.requireNonNull(extension);
    this.slashmapper = Objects.requireNonNull(slashmapper);
  }

  @Override
  public void queue() {
    if (isQueue)
      return;
    tree.addSlashMapper(slashmapper);
    isQueue = true;
  }
}
