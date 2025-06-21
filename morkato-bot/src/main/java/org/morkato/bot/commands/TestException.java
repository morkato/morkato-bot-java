package org.morkato.bot.commands;

import org.morkato.api.ApiConnectionStatement;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.context.CommandContext;
import org.morkato.boot.annotation.AutoInject;
import org.morkato.bmt.NoArgs;

public class TestException implements CommandHandler<NoArgs> {
  @AutoInject
  ApiConnectionStatement statement;
  @Override
  public void invoke(CommandContext<NoArgs> ctx) {
    throw new RuntimeException("Um erro inesperado!");
  }
}
