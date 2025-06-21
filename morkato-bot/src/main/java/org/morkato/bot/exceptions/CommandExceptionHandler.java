package org.morkato.bot.exceptions;

import org.morkato.bmt.context.CommandContext;

public class CommandExceptionHandler implements org.morkato.bmt.components.CommandExceptionHandler<Exception>{
  @Override
  public void doException(CommandContext<?> ctx, Exception exception) {
    ctx.respond()
      .setContent("Um erro inesperado ocorreu: **" + exception.getClass().getName() + "** -- \"" + exception.getMessage() + "\". Por favor, notifique a meu desenvolvedor.")
      .queue();
  }
}
