package org.morkato.bot.slashcommands;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.context.CommandContext;

public class TestSlash implements CommandHandler<String>{
  @Override
  public void invoke(CommandContext<String> context) {
    context.reply()
      .setContent("Isto é uma resposta distinta do slash! Argumento passado: **" + context.getDefinedArguments() + "**")
      .queue();
  }
}
