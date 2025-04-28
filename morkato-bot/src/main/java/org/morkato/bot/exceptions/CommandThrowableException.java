package org.morkato.bot.exceptions;

import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.CommandException;

public class CommandThrowableException implements CommandException<Throwable> {
  @Override
  public void doException(TextCommandContext<?> ctx,Throwable exception) {
    ctx.sendMessage("Um erro inesperado ocorreu: **" + exception.getClass().getName() + "** -- \"" + exception.getMessage() + "\". Por favor, notifique a meu desenvolvedor.").queue();
  }
}
