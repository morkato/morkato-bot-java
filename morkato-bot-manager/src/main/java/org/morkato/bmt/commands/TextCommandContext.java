package org.morkato.bmt.commands;

import org.morkato.bmt.exception.StupidArgumentException;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import org.morkato.bmt.components.CommandHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.morkato.bmt.BotCore;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TextCommandContext<Args> implements CommandContext<Args> {
  protected final BotCore core;
  protected final CommandRegistry<Args> command;
  protected final String invokedCommandName;
  protected final boolean fromGuild;
  protected Args args;
  protected Guild guild;
  protected Message message;
  protected MessageChannel channel;
  protected Member member;
  protected User author;

  public TextCommandContext(
    @Nonnull BotCore core,
    @Nonnull CommandRegistry<Args> command,
    @Nonnull Message message,
    @Nonnull String invokedCommandName
  ) {
    this.core = core;
    this.fromGuild = message.isFromGuild();
    this.command = command;
    this.invokedCommandName = invokedCommandName;
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
  public CommandHandler<Args> getCommandHandler() {
    return command.getCommandHandler();
  }

  @Override
  @Nonnull
  public CommandRegistry<Args> getTextCommandRegistry() {
    return command;
  }

  @Override
  @Nonnull
  public SlashCommandRegistry<Args> getSlashCommandRegistry() {
    /* TODO: Adicionar uma mensagem aqui. */
    throw new IllegalStateException("");
  }

  @Override
  @Nonnull
  public String getInvokedCommandName() {
    return invokedCommandName;
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

  @Override
  @Nonnull
  public BotCore getBotCore() {
    return core;
  }
}
