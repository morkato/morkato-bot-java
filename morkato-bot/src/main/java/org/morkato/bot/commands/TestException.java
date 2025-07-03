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
