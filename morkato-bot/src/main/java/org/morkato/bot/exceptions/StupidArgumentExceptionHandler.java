package org.morkato.bot.exceptions;

import org.morkato.mcbmt.exception.StupidArgumentException;
import org.morkato.mcbmt.components.CommandExceptionHandler;
import org.morkato.mcbmt.commands.CommandContext;

public class StupidArgumentExceptionHandler implements CommandExceptionHandler<StupidArgumentException>{
  @Override
  public void doException(CommandContext<?> ctx, StupidArgumentException exception) {
    ctx.reply()
      .setContent("Este comando espera àlguns argumentos. Parece que você não os passou.")
      .mentionRepliedUser(false)
      .queue();
  }
}
