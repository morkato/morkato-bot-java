package org.morkato.mcbmt.commands.context;

import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.mcbmt.actions.ActionSession;
import org.morkato.mcbmt.actions.LayoutAction;
import org.morkato.mcbmt.actions.LayoutActionSessionImpl;
import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.commands.CommandContext;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;

public class TextMessageCreation<ContextType>
  extends SessionableMessageCreation<ContextType>
  implements MessageCreation<ContextType> {
  private final MessageCreateAction action;

  public TextMessageCreation(CommandContext<ContextType> ctx) {
    super(ctx);
    this.action = new MessageCreateActionImpl(ctx.getChannel());
  }

  @Override
  protected void syncBuilderWithLayout(LayoutAction layout) {
    action.addComponents(layout.build());
  }

  @Override
  public MessageCreation<ContextType> setContent(String content) {
    action.setContent(content);
    return this;
  }

  @Override
  public MessageCreation<ContextType> addEmbed(MessageEmbed embed) {
    action.addEmbeds(embed);
    return this;
  }

  @Override
  public MessageCreation<ContextType> mentionRepliedUser(boolean m) {
    action.mentionRepliedUser(m);
    return this;
  }

  @Override
  public MessageCreation<ContextType> setMessageReference(String id) {
    action.setMessageReference(id);
    return this;
  }

  @Override
  public MessageCreation<ContextType> allowedMentions(Collection<Message.MentionType> mentions) {
    action.setAllowedMentions(mentions);
    return this;
  }

  @Override
  public void queue() {
    this.action.queue(this::dispatchQueuedMessage);
  }
}
