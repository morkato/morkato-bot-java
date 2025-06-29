package org.morkato.bmt.startup.builder;

import org.morkato.bmt.startup.attributes.CommandAttributes;
import org.morkato.bmt.startup.payload.CommandPayload;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.BotContext;
import org.morkato.boot.Extension;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TextCommandBuilder<T> {
  private final Set<String> alias = new HashSet<>();
  private final AppBuilder.AppBuilderImpl builder;
  private final Extension<BotContext> extension;
  private final Class<? extends CommandHandler<T>> command;
  private String name;
  private String description;
  private boolean isQueue = false;

  public TextCommandBuilder(
    AppBuilder.AppBuilderImpl builder,
    Extension<BotContext> extension,
    Class<? extends CommandHandler<T>> command
  ) {
    this.builder = Objects.requireNonNull(builder);
    this.extension = Objects.requireNonNull(extension);
    this.command = Objects.requireNonNull(command);
  }

  public TextCommandBuilder<T> setName(String name) {
    this.name = name;
    return this;
  }

  public TextCommandBuilder<T> setDescription(String desc) {
    this.description = desc;
    return this;
  }

  public TextCommandBuilder<T> addAlias(String name) {
    alias.add(name);
    return this;
  }

  public TextCommandBuilder<T> commandAlias(String alias) {
    return this;
  }

  public <S> TextCommandBuilder<T> subcommand(String name, CommandHandler<S> subcommand) {
    return this;
  }

  public void queue() {
    if (isQueue)
      return;
    this.builder.addCommand(new CommandPayload<>(command, new CommandAttributes(name, alias.toArray(String[]::new), description, CommandHandler.getArgument(command))));
    isQueue = true;
  }
}
