package org.morkato.bmt.commands.context;

import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.actions.ActionSession;
import org.morkato.bmt.actions.LayoutActionSessionImpl;
import org.morkato.bmt.components.ActionHandler;
import org.morkato.bmt.commands.CommandContext;

import java.util.Collection;

public class TextMessageCreation implements MessageCreation {
  private final CommandContext<?> ctx;
  private final MessageCreateAction action;
  private boolean dispatchActionSession = false;
  private int sessionSlot = -1;

  public TextMessageCreation(CommandContext<?> ctx) {
    this.action = new MessageCreateActionImpl(ctx.getChannel());
    this.ctx = ctx;
  }

  private void dispatchMessage(Message message) {
    if (!dispatchActionSession)
      return;
    ctx.getBotCore().setSessionMessage(sessionSlot, message);
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
  @SuppressWarnings("unchecked")
  public <T> MessageCreation setActionSession(ActionHandler<T> actionhandler, T payload) {
    if (dispatchActionSession)
      return this;
    final LayoutActionSessionImpl layout = new LayoutActionSessionImpl();
    final ActionSession<T> session = ctx.getBotCore().createSession(
      layout,
      actionhandler,
      ctx.getAuthor(),
      payload
    );
    layout.setSessionSlot(session.getSlot());
    actionhandler.registerAction(layout);
    this.sessionSlot = session.getSlot();
    this.dispatchActionSession = true;
    action.addComponents(layout.build());
    return this;
  }

  @Override
  public void queue() {
    this.action.queue(this::dispatchMessage);
  }
}
