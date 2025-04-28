package org.morkato.bmt.internal.context.builder;

import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.internal.context.BotContextInternal;
import org.morkato.bmt.registration.attributes.CommandAttributes;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.context.builder.TextCommandBuilder;
import org.morkato.bmt.components.Command;
import org.morkato.boot.Extension;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TextCommandBuilderInternal<T> implements TextCommandBuilder<T> {
  private final Set<String> names = new HashSet<>();
  private final BotContextInternal builder;
  private final Extension<BotContext> extension;
  private final Command<T> command;
  private boolean isQueue = false;

  public TextCommandBuilderInternal(BotContextInternal builder, Extension<BotContext> extension, Command<T> command) {
    this.builder = Objects.requireNonNull(builder);
    this.extension = Objects.requireNonNull(extension);
    this.command = Objects.requireNonNull(command);
  }

  @Override
  public TextCommandBuilder<T> addName(String name) {
    names.add(name);
    return this;
  }

  @Override
  public void queue() {
    if (isQueue)
      return;
    this.builder.addCommand(new CommandPayload<>(command, new CommandAttributes(names.toArray(String[]::new), null)));
    isQueue = true;
  }
}
