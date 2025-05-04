package org.morkato.bmt.context;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import org.morkato.bmt.action.MessageCreation;
import org.morkato.bmt.components.CommandHandler;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.morkato.bmt.internal.action.SlashMessageCreationInternal;
import org.morkato.bmt.internal.action.TextMessageCreationInternal;

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
  CommandHandler<Args> getCommand();
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

  default void deferReplyIfInteraction(boolean ep) {}
  default void deferReplyIfInteraction() {}

  default MessageCreation respond() {
    if (this.isInvokedTextCommand())
      return new TextMessageCreationInternal(this);
    return new SlashMessageCreationInternal(this);
  }

  default MessageCreation reply() {
    if (this.isInvokedTextCommand())
      return new TextMessageCreationInternal(this)
        .setMessageReference(this.getMessage());
    return new SlashMessageCreationInternal(this);
  }
}
