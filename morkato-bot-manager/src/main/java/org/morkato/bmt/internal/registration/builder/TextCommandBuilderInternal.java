package org.morkato.bmt.internal.registration.builder;

import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.internal.registration.AppCommandTreeInternal;
import org.morkato.bmt.registration.attributes.CommandAttributes;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.registration.builder.TextCommandBuilder;
import org.morkato.bmt.components.Command;
import org.morkato.boot.Extension;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TextCommandBuilderInternal<T> implements TextCommandBuilder<T> {
  private final Set<String> alias = new HashSet<>();
  private final AppCommandTreeInternal builder;
  private final Extension<BotContext> extension;
  private final Command<T> command;
  private String name;
  private String description;
  private boolean isQueue = false;

  public TextCommandBuilderInternal(AppCommandTreeInternal builder, Extension<BotContext> extension, Command<T> command) {
    this.builder = Objects.requireNonNull(builder);
    this.extension = Objects.requireNonNull(extension);
    this.command = Objects.requireNonNull(command);
  }

  @Override
  public TextCommandBuilder<T> setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public TextCommandBuilder<T> setDescription(String desc) {
    this.description = desc;
    return this;
  }

  @Override
  public TextCommandBuilder<T> addAlias(String name) {
    alias.add(name);
    return this;
  }


  @Override
  public TextCommandBuilder<T> commandAlias(String alias) {
    return this;
  }

  @Override
  public <S> TextCommandBuilder<T> subcommand(String name, Command<S> subcommand) {
    return this;
  }

  @Override
  public void queue() {
    if (isQueue)
      return;
    this.builder.addCommand(new CommandPayload<>(command, new CommandAttributes(name, alias.toArray(String[]::new), description)));
    isQueue = true;
  }
}
