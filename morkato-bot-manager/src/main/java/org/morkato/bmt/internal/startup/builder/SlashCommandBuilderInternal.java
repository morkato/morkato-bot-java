package org.morkato.bmt.internal.startup.builder;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.internal.startup.AppCommandTreeInternal;
import org.morkato.bmt.startup.builder.SlashCommandBuilder;
import org.morkato.bmt.startup.attributes.SlashCommandAttributes;
import org.morkato.bmt.startup.payload.SlashCommandPayload;

import java.util.Objects;

public class SlashCommandBuilderInternal<T> implements SlashCommandBuilder<T> {
  private final AppCommandTreeInternal tree;
  private final CommandHandler<T> slashcommand;
  private boolean isQueue = false;
  private String name;
  private String description;
  private int flags = 0;

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
  public SlashCommandBuilder<T> deferReply(boolean ep) {
    this.flags |= (1 << 1);
    this.flags |= ((ep ? 1 : 0) << 2);
    return this;
  }

  @Override
  public void queue() {
    if (isQueue)
      return;
    tree.addSlashCommand(new SlashCommandPayload<>(slashcommand, new SlashCommandAttributes(name, description, flags, CommandHandler.getArgument(slashcommand.getClass()))));
    isQueue = true;
  }
}
