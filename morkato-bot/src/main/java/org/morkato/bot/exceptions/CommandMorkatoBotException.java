package org.morkato.bot.exceptions;

import org.morkato.bmt.components.CommandExceptionHandler;
import org.morkato.bmt.commands.CommandContext;

public class CommandMorkatoBotException implements CommandExceptionHandler<MorkatoBotException>{
  @Override
  public void doException(CommandContext<?> ctx, MorkatoBotException exception) {
    ctx.reply()
      .setContent(exception.getMessage())
      .queue();
  }
}
