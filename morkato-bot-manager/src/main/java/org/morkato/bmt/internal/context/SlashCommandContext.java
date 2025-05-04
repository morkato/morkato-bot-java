package org.morkato.bmt.internal.context;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.context.CommandContext;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SlashCommandContext<Args> implements CommandContext<Args> {
  protected final boolean fromGuild;
  protected final CommandInteraction interaction;
  protected final Args args;
  protected Guild guild;
  protected CommandHandler<Args> command;
  protected MessageChannel channel;
  protected Member member;
  protected User author;

  public SlashCommandContext(CommandInteraction interaction, CommandHandler<Args> command, Args result) {
    this.fromGuild = interaction.isFromGuild();
    this.interaction = interaction;
    this.command = command;
    this.args = result;
    this.channel = interaction.getMessageChannel();
    this.author = interaction.getUser();
    if (this.fromGuild) {
      this.guild = interaction.getGuild();
      this.member = interaction.getMember();
    }
  }

  @Override
  public boolean isFromGuild() {
    return fromGuild;
  }

  @Override
  public boolean isDefinedArguments() {
    return Objects.nonNull(args);
  }

  @Override
  public boolean isInvokedInteractionCommand() {
    return true;
  }

  @Override
  public boolean isInvokedTextCommand() {
    return false;
  }

  @Override
  @Nonnull
  public Args getDefinedArguments() {
    return Objects.requireNonNull(args);
  }

  @Override
  @Nonnull
  public CommandInteraction getInteraction() {
    return interaction;
  }

  @Override
  @Nonnull
  public CommandHandler<Args> getCommand() {
    return command;
  }

  @Override
  @Nonnull
  public Message getMessage() {
    throw new IllegalStateException("This context is a interaction response!");
  }

  @Override
  @Nonnull
  public MessageChannel getChannel() {
    return channel;
  }

  @Override
  @Nonnull
  public User getAuthor() {
    return author;
  }

  @Override
  @Nonnull
  public Member getMember() {
    return Objects.requireNonNull(member);
  }

  @Override
  @Nonnull
  public Guild getGuild() {
    return Objects.requireNonNull(guild);
  }

  @Override
  public void deferReplyIfInteraction(boolean ep){
    this.getInteraction().deferReply(ep).queue();
  }

  @Override
  public void deferReplyIfInteraction() {
    this.deferReplyIfInteraction(false);
  }
}
