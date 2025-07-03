package org.morkato.mcbmt.commands;

import org.morkato.mcbmt.generated.registries.CommandRegistry;
import org.morkato.mcbmt.generated.registries.SlashCommandRegistry;
import org.morkato.mcbmt.commands.context.SlashMessageCreation;
import org.morkato.mcbmt.commands.context.TextMessageCreation;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.morkato.mcbmt.components.CommandHandler;
import org.morkato.mcbmt.BotCore;
import org.morkato.mcbmt.commands.context.MessageCreation;
import javax.annotation.Nonnull;

public interface CommandContext<Args> {
  boolean isFromGuild();
  boolean isDefinedArguments();
  boolean isInvokedInteractionCommand();
  boolean isInvokedTextCommand();
  @Nonnull
  Args getDefinedArguments();
  @Nonnull
  CommandInteraction getInteraction();
  @Nonnull
  CommandHandler<Args> getCommandHandler();
  @Nonnull
  CommandRegistry<Args> getTextCommandRegistry();
  @Nonnull
  SlashCommandRegistry<Args> getSlashCommandRegistry();
  @Nonnull
  String getInvokedCommandName();
  @Nonnull
  Message getMessage();
  @Nonnull
  MessageChannel getChannel();
  @Nonnull
  User getAuthor();
  @Nonnull
  Member getMember();
  @Nonnull
  Guild getGuild();
  @Nonnull
  BotCore getBotCore();

  default void deferReplyIfInteraction(boolean ep) {}
  default void deferReplyIfInteraction() {}

  default MessageCreation respond() {
    if (this.isInvokedTextCommand())
      return new TextMessageCreation(this);
    return new SlashMessageCreation(this);
  }

  default MessageCreation reply() {
    if (this.isInvokedTextCommand())
      return new TextMessageCreation(this)
        .setMessageReference(this.getMessage());
    return new SlashMessageCreation(this);
  }
}
