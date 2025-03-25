package org.morkato.bmt.impl;

import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.TextCommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.morkato.bmt.exception.StupidArgumentException;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TextCommandContextImpl<Args> implements TextCommandContext<Args> {
  protected boolean fromGuild;
  protected Args args;
  protected Guild guild;
  protected Command<?> command;
  protected Message message;
  protected MessageChannel channel;
  protected Member member;
  protected User author;

  public TextCommandContextImpl(
    @Nonnull Command<?> command,
    @Nonnull Message message,
    Args args
    ) {
    this.fromGuild = message.isFromGuild();
    this.args = args;
    this.command = command;
    this.message = message;
    this.channel = message.getChannel();
    this.author = message.getAuthor();
    if (this.fromGuild) {
      this.guild = message.getGuild();
      this.channel = message.getGuildChannel();
      this.member = message.getMember();
    }
  }
  @Override
  public boolean isDefinedArguments() {
    return Objects.nonNull(args);
  }
  @Override
  @Nonnull
  public Args getDefinedArguments() {
    if (Objects.isNull(args))
      throw new StupidArgumentException();
    return this.args;
  }
  public void setArgs(Args args) {
    this.args = args;
  }
  @Override
  public boolean isFromGuild() {
    return this.fromGuild;
  }
  @Override
  @Nonnull
  public Command<?> getCommand() {
    return this.command;
  }
  @Override
  @Nonnull
  public Message getMessage() {
    return this.message;
  }
  @Override
  @Nonnull
  public MessageChannel getChannel() {
    return this.channel;
  }
  @Override
  @Nonnull
  public User getAuthor() {
    return this.author;
  }
  @Override
  @Nonnull
  public Guild getGuild() {
    Objects.requireNonNull(guild);
    return this.guild;
  }

  @Override
  @Nonnull
  public Member getMember(){
    Objects.requireNonNull(guild);
    return this.member;
  }

  @Override
  @Nonnull
  public MessageCreateAction createMessage() {
    return new MessageCreateActionImpl(channel);
  }

  @Override
  @Nonnull
  public MessageCreateAction sendMessage(CharSequence seq) {
    return this.channel.sendMessage(seq);
  }

  @Override
  @Nonnull
  public MessageCreateAction replyMessage(CharSequence seq) {
    return this.message.reply(seq);
  }
}