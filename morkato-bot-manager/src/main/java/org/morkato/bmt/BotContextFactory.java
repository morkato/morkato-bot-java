package org.morkato.bmt;

import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.internal.context.BotContextInternal;
import org.morkato.bmt.internal.registration.BotRegistrationFactory;
import org.morkato.boot.ExtensionContextFactory;
import org.morkato.bmt.context.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class BotContextFactory implements ExtensionContextFactory<BotContext> {
  private final BotRegistrationFactory factory;

  public BotContextFactory(BotRegistrationFactory factory) {
    this.factory = Objects.requireNonNull(factory);
  }

  @Override
  public BotContext make(Extension<BotContext> extension) {
    return new BotContextInternal(extension);
  }

  @Override
  public void commit(Extension<BotContext> extension, BotContext context) {
    for (ObjectParser<?> parser : context.getPendingArguments())
      factory.registerObjectParser(parser);
    for (CommandPayload<?> payload : context.getPendingCommands())
      factory.registerCommand(payload.command(), payload.attrs());
    for (CommandException<?> bounder : context.getPendingCommandExceptions())
      factory.registerCommandException(bounder);
  }
}
