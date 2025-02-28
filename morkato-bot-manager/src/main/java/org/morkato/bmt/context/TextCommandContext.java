package org.morkato.bmt.context;

import org.morkato.bmt.components.Command;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public interface TextCommandContext<Args> {
  boolean isFromGuild();
  boolean isPresentArgs();
  @Nonnull
  Args getArgs();
  @Nonnull
  Command getCommand();
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
  MessageCreateAction send(CharSequence content);
  @Nonnull
  MessageCreateAction reply(CharSequence content);
}
