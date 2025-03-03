package org.morkato.bmt.impl;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.StupidArgumentException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

import javax.annotation.Nonnull;

public class ContextImpl<Args> implements TextCommandContext<Args>{
  protected boolean fromGuild = false;
  protected Args args;
  protected Guild guild;
  protected Command command;
  protected Message message;
  protected MessageChannel channel;
  protected Member member;
  protected User author;

  public ContextImpl(
    @Nonnull Command command,
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
  public boolean isPresentArgs() {
    return this.args != null;
  }
  @Override
  public Args getArgs() {
    if (this.args == null) {
      throw new StupidArgumentException();
    }
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
  public Command getCommand() {
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
    if (this.guild == null) {
      throw new IllegalStateException();
    }
    return this.guild;
  }
  @Override
  @Nonnull
  public Member getMember() {
    if (this.guild == null) {
      throw new IllegalStateException();
    }
    return this.member;
  }
  @Override
  @Nonnull
  public MessageCreateAction send(CharSequence content) {
    return channel.sendMessage(content);
  }
  @Override
  @Nonnull
  public MessageCreateAction reply(CharSequence content) {
    return this.send(content).setMessageReference(message);
  }
}