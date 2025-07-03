package org.morkato.mcbmt.startup.builder;

import org.morkato.mcbmt.startup.attributes.SlashCommandAttributes;
import org.morkato.mcbmt.startup.payload.SlashCommandPayload;
import org.morkato.mcbmt.components.CommandHandler;

import java.util.Objects;

public class SlashCommandBuilder<T> {
  private final AppBuilder.AppBuilderImpl tree;
  private final Class<? extends CommandHandler<T>> slashcommand;
  private boolean isQueue = false;
  private String name;
  private String description;
  private int flags = 0;

  public SlashCommandBuilder(
    AppBuilder.AppBuilderImpl tree,
    String name,
    Class<? extends CommandHandler<T>> slashcommand
  ) {
    this.slashcommand = Objects.requireNonNull(slashcommand);
    this.tree = Objects.requireNonNull(tree);
    this.name = name;
  }

  public SlashCommandBuilder<T> setName(String name) {
    this.name = name;
    return this;
  }

  public SlashCommandBuilder<T> setDescription(String description) {
    this.description = description;
    return this;
  }

  public SlashCommandBuilder<T> deferReply(boolean ep) {
    this.flags |= (1 << 1);
    this.flags |= ((ep ? 1 : 0) << 2);
    return this;
  }

  public SlashCommandBuilder<T> deferReply() {
    return this.deferReply(false);
  }

  public void queue() {
    if (isQueue)
      return;
    tree.addSlashCommand(new SlashCommandPayload<>(slashcommand, new SlashCommandAttributes(name, description, flags, CommandHandler.getArgument(slashcommand))));
    isQueue = true;
  }
}
