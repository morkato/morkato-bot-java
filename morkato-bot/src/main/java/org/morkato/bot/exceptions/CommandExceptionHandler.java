package org.morkato.bot.exceptions;

import org.morkato.mcbmt.commands.CommandContext;

public class CommandExceptionHandler implements org.morkato.mcbmt.components.CommandExceptionHandler<Exception>{
  @Override
  public void doException(CommandContext<?> ctx, Exception exception) {
    ctx.respond()
      .setContent("Um erro inesperado ocorreu: **" + exception.getClass().getName() + "** -- \"" + exception.getMessage() + "\". Por favor, notifique a meu desenvolvedor.")
      .queue();
  }
}
