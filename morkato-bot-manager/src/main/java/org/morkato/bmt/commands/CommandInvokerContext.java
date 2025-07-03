package org.morkato.bmt.commands;

import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.util.StringView;

import java.util.Objects;

public class CommandInvokerContext {
  private final Message message;
  private final StringView view;

  public static CommandInvokerContext from(Message message, StringView view) {
    return new CommandInvokerContext(message, view);
  }

  private CommandInvokerContext(Message message, StringView view) {
    Objects.requireNonNull(message);
    Objects.requireNonNull(view);
    this.message = message;
    this.view = view;
  }

  public Message getMessage() {
    return message;
  }

  public StringView getView() {
    return view;
  }
}
