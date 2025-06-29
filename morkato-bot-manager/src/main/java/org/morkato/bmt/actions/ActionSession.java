package org.morkato.bmt.actions;

import org.morkato.bmt.components.ActionHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.TimeUnit;

public interface ActionSession<T> {
  LayoutAction getLayout();
  ActionHandler<T> getActionHandler();
  Message getMessage();
  User getAuthor();
  T getPayload();
  void setDisabledButton(ButtonAction button, boolean disabled);
  long expiresOn();
  void keepalive(long milis);
  int getSlot();
  void close();

  default void cleanLayout() {
    this.getMessage()
      .editMessageComponents()
      .queue();
  }

  default boolean isExpired() {
    return System.currentTimeMillis() >= this.expiresOn();
  }

  default void keepalive(long unknown, TimeUnit time) {
    this.keepalive(time.toMillis(unknown));
  }

  default void disableButton(ButtonAction action) {
    this.setDisabledButton(action, true);
  }

  default void enableButton(ButtonAction action) {
    this.setDisabledButton(action, false);
  }
}
