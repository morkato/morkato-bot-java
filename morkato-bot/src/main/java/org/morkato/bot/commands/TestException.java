package org.morkato.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.commands.CommandContext;
import org.morkato.bmt.generated.registries.ActionRegistry;
import org.morkato.bmt.startup.CommandsInitialization;
import org.morkato.bmt.NoArgs;
import org.morkato.bot.action.ResponseTest;

public class TestException implements CommandHandler<NoArgs> {
  private ActionRegistry<String> action;
  @Override
  public void invoke(CommandContext<NoArgs> ctx) {
    throw new RuntimeException("A error");
  }

  @Override
  public void initialize(CommandsInitialization init) {
    action = init.action(ResponseTest.class);
    /* tipo um... "init.message("prop", message)" */
  }

  public void onMessageOriginalDispatched(CommandContext<NoArgs> ctx, Message message) {
    /* LÓGICA */
  }
}
