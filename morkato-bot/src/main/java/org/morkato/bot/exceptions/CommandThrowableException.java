package org.morkato.bot.exceptions;

import org.morkato.bmt.context.CommandContext;
import org.morkato.bmt.components.CommandException;

public class CommandThrowableException implements CommandException<Throwable> {
  @Override
  public void doException(CommandContext<?> ctx, Throwable exception) {
    ctx.reply()
      .setContent("Um erro inesperado ocorreu: **" + exception.getClass().getName() + "** -- \"" + exception.getMessage() + "\". Por favor, notifique a meu desenvolvedor.")
      .queue();
  }
}
