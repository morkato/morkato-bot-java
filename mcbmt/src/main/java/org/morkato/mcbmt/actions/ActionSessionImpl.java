package org.morkato.mcbmt.actions;

import org.morkato.mcbmt.components.ActionHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class ActionSessionImpl<T> implements ActionSession<T> {
  private final ActionSessionManagement sessions;
  private final LayoutAction layout;
  private final User author;
  private final T payload;
  private final int slot;
  private long expireOn;
  private ActionHandler<T> actionhandler;
  private Message message;

  public ActionSessionImpl(
    ActionSessionManagement sessions,
    LayoutAction layout,
    ActionHandler<T> actionhandler,
    User author,
    T payload,
    long expireOn,
    int slot
  ) {
    this.sessions = sessions;
    this.layout = layout;
    this.actionhandler = actionhandler;
    this.author = author;
    this.payload = payload;
    this.expireOn = expireOn;
    this.slot = slot;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  @Override
  public LayoutAction getLayout() {
    return layout;
  }

  @Override
  public ActionHandler<T> getActionHandler() {
    return actionhandler;
  }

  @Override
  public Message getMessage() {
    return message;
  }

  @Override
  public User getAuthor() {
    return author;
  }

  @Override
  public T getPayload() {
    return payload;
  }

  @Override
  public void setDisabledButton(ButtonAction button, boolean disabled){

  }

  @Override
  public long expiresOn() {
    return expireOn;
  }

  @Override
  public void keepalive(long milis) {
    this.expireOn = System.currentTimeMillis() + milis;
  }

  @Override
  public int getSlot() {
    return slot;
  }

  @Override
  public void close() {
    sessions.flushSession(this.getSlot());
  }
}
