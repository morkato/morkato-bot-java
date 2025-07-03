package org.morkato.bot.commands;

import org.morkato.mcbmt.components.CommandHandler;
import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.generated.registries.ActionRegistry;
import org.morkato.mcbmt.startup.CommandsInitialization;
import org.morkato.mcbmt.NoArgs;
import org.morkato.bot.action.ResponseTest;

public class TestException implements CommandHandler<NoArgs> {
  private ActionRegistry<String> action;
  @Override
  public void invoke(CommandContext<NoArgs> ctx) {
    ctx.reply()
      .setContent("Amoeba")
      .mentionRepliedUser(false)
      .setActionSession(action, "a")
      .queue();
  }

  @Override
  public void initialize(CommandsInitialization init) {
    action = init.action(ResponseTest.class);
  }
}
