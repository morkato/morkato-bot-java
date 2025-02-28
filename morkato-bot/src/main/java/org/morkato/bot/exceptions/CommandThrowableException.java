package org.morkato.bot.exceptions;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.CommandException;

@MorkatoComponent
public class CommandThrowableException implements CommandException<Throwable> {
  @Override
  public void doException(TextCommandContext<?> ctx,Throwable exception) {
    ctx.send("Um erro inesperado ocorreu: **" + exception.getClass().getName() + "**. Por favor, notifique a meu desenvolvedor.").queue();
  }
}
