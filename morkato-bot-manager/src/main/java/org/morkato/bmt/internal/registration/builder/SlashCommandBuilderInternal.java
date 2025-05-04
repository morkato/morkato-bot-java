package org.morkato.bmt.internal.registration.builder;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.internal.registration.AppCommandTreeInternal;
import org.morkato.bmt.registration.builder.SlashCommandBuilder;
import org.morkato.bmt.registration.attributes.SlashCommandAttributes;
import org.morkato.bmt.registration.payload.SlashCommandPayload;

import java.util.Objects;

public class SlashCommandBuilderInternal<T> implements SlashCommandBuilder<T> {
  private final AppCommandTreeInternal tree;
  private final CommandHandler<T> slashcommand;
  private boolean isQueue = false;
  private String name;
  private String description;

  public SlashCommandBuilderInternal(AppCommandTreeInternal tree, String name, CommandHandler<T> slashcommand) {
    this.slashcommand = Objects.requireNonNull(slashcommand);
    this.tree = Objects.requireNonNull(tree);
    this.name = name;
  }

  @Override
  public SlashCommandBuilder<T> setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public SlashCommandBuilder<T> setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public void queue() {
    if (isQueue)
      return;
    tree.addSlashCommand(new SlashCommandPayload<>(slashcommand, new SlashCommandAttributes(name, description)));
    isQueue = true;
  }
}
