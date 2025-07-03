package org.morkato.mcbmt.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public interface ActionContext<T> {
  boolean hasButtonClicked(ButtonAction action);
  boolean isActiveSession();
  ActionSession<T> getActiveSession();
  void deferReply(boolean ep);
  void deferEdit();
  ReplyCallbackAction reply(String content);
  WebhookMessageCreateAction<Message> sendMessage(String content);
  MessageEditAction editMessage(String content);

  default void deferReply() {
    this.deferReply(false);
  }
}
