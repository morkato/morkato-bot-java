package org.morkato.bmt.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.Objects;

public class ButtonClickedActionContext<T> implements ActionContext<T> {
  private final ButtonInteractionEvent interaction;
  private final ActionSession<T> session;
  private final ButtonAction button;
  private final String componentId;

  public ButtonClickedActionContext(
    ButtonInteractionEvent interaction,
    ActionSession<T> session,
    ButtonAction button,
    String componentId
  ) {
    this.interaction = interaction;
    this.session = session;
    this.button = button;
    this.componentId = componentId;
  }

  public ButtonClickedActionContext(
    ButtonInteractionEvent interaction,
    ButtonAction button,
    String componentId
  ) {
    this(interaction, null, button, componentId);
  }

  @Override
  public boolean isActiveSession() {
    return Objects.nonNull(session);
  }

  @Override
  public ActionSession<T> getActiveSession(){
    return session;
  }

  @Override
  public boolean hasButtonClicked(ButtonAction action) {
    return Objects.equals(button, action);
  }

  @Override
  public void deferReply(boolean ep) {
    if (!interaction.isAcknowledged())
      interaction.deferReply(ep).queue();
  }

  @Override
  public void deferEdit() {
    interaction.deferEdit().queue();
  }

  @Override
  public ReplyCallbackAction reply(String content) {
    return interaction.reply(content);
  }

  @Override
  public WebhookMessageCreateAction<Message> sendMessage(String content) {
    return interaction.getHook().sendMessage(content);
  }

  @Override
  public MessageEditAction editMessage(String content) {
    return session.getMessage().editMessage(content);
  }
}
