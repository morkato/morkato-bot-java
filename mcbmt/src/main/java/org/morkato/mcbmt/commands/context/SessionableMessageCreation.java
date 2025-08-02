package org.morkato.mcbmt.commands.context;

import net.dv8tion.jda.api.entities.Message;
import org.morkato.mcbmt.actions.ActionSession;
import org.morkato.mcbmt.actions.LayoutAction;
import org.morkato.mcbmt.actions.LayoutActionSessionImpl;
import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.components.ActionHandler;

import java.util.function.BiConsumer;
import java.util.Objects;

public abstract class SessionableMessageCreation<T> implements MessageCreation<T> {
  protected abstract void syncBuilderWithLayout(LayoutAction layout);

  protected final CommandContext<T> ctx;
  private BiConsumer<CommandContext<T>, Message> dispatcher;
  private int sessionSlot = -1;

  public SessionableMessageCreation(CommandContext<T> ctx) {
    this.ctx = ctx;
  }

  protected void dispatchQueuedMessage(Message message) {
    if (sessionSlot != -1)
      ctx.getBotCore().setSessionMessage(sessionSlot, message);
    if (Objects.nonNull(dispatcher))
      dispatcher.accept(ctx, message);
  }

  @Override
  public MessageCreation<T> dispatch(BiConsumer<CommandContext<T>, Message> dispatcher) {
    this.dispatcher = dispatcher;
    return this;
  }

  @Override
  public <PayloadType> MessageCreation<T> setActionSession(ActionHandler<PayloadType> actionhandler, PayloadType payload) {
    if (sessionSlot != -1)
      return this;
    final LayoutActionSessionImpl layout = new LayoutActionSessionImpl();
    final ActionSession<PayloadType> session = ctx.getBotCore().createSession(
      layout,
      actionhandler,
      ctx.getAuthor(),
      payload
    );
    layout.setSessionSlot(session.getSlot());
    actionhandler.registerAction(layout);
    this.sessionSlot = session.getSlot();
    this.syncBuilderWithLayout(layout);
    return this;
  }
}
