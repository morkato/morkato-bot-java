package org.morkato.bmt.internal.context;

import org.morkato.bmt.exception.StupidArgumentException;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.context.CommandContext;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import javax.annotation.Nonnull;
import java.util.Objects;

public class TextCommandContext<Args> implements CommandContext<Args> {
  protected boolean fromGuild;
  protected Args args;
  protected Guild guild;
  protected CommandHandler<Args> command;
  protected Message message;
  protected MessageChannel channel;
  protected Member member;
  protected User author;

  public TextCommandContext(
    @Nonnull CommandHandler<Args> command,
    @Nonnull Message message,
    Args args
  ){
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

  public void setArgs(Args args) {
    this.args = args;
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
    return false;
  }

  @Override
  public boolean isInvokedTextCommand() {
    return true;
  }

  @Override
  @Nonnull
  public Args getDefinedArguments() {
    if (Objects.isNull(args))
      throw new StupidArgumentException();
    return args;
  }

  @Override
  @Nonnull
  public CommandInteraction getInteraction() {
    throw new IllegalStateException("Interaction command are not invoked.");
  }

  @Override
  @Nonnull
  public CommandHandler<Args> getCommand() {
    return command;
  }

  @Override
  @Nonnull
  public Message getMessage() {
    return message;
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
}
