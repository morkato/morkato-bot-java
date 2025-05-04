package org.morkato.bmt.internal.action;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;
import org.morkato.bmt.action.MessageCreation;
import org.morkato.bmt.context.CommandContext;

import java.util.Collection;
import java.util.List;

public class TextMessageCreationInternal implements MessageCreation {
  private final MessageCreateAction action;

  public TextMessageCreationInternal(CommandContext<?> ctx) {
    this.action = new MessageCreateActionImpl(ctx.getChannel());
  }

  @Override
  public MessageCreation setContent(String content) {
    action.setContent(content);
    return this;
  }

  @Override
  public MessageCreation addEmbed(MessageEmbed embed) {
    action.addEmbeds(embed);
    return this;
  }

  @Override
  public MessageCreation mentionRepliedUser(boolean m) {
    action.mentionRepliedUser(m);
    return this;
  }

  @Override
  public MessageCreation setMessageReference(String id) {
    action.setMessageReference(id);
    return this;
  }

  @Override
  public MessageCreation allowedMentions(Collection<Message.MentionType> mentions) {
    action.setAllowedMentions(mentions);
    return this;
  }

  @Override
  public void queue() {
    action.queue();
  }
}
