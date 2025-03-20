package org.morkato.bmt.bmt.listener;

import org.morkato.bmt.bmt.registration.impl.MorkatoBotManagerRegistration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.morkato.bmt.bmt.context.invoker.CommandInvokerContext;
import org.morkato.bmt.bmt.invoker.Invoker;
import org.morkato.bmt.utility.StringView;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Objects;

public abstract class TextCommandListener extends MorkatoListenerAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandListener.class);
  private final Invoker<CommandInvokerContext> invoker;
  public abstract String getPrefix();

  public TextCommandListener(MorkatoBotManagerRegistration registration, Invoker<CommandInvokerContext> invoker) {
    super(registration);
    Objects.requireNonNull(invoker);
    this.invoker = invoker;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    /* Yup, yup! Lê a mensagem recebida e responde o usuário, caso o bot reconheça o comando, e caso ele esteja pronto para executar comandos! */
    if (!invoker.isReady())
      return;
    final String prefix = this.getPrefix();
    final StringView view = new StringView(event.getMessage().getContentRaw());
    final String supposedPrefix = view.read(prefix.length());
    if (!prefix.equals(supposedPrefix))
      return;
    final CommandInvokerContext context = CommandInvokerContext.from(event.getMessage(), view);
    invoker.invoke(context);
  }
}