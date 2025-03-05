package org.morkato.bot.exceptions;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.context.TextCommandContext;

@MorkatoComponent
public class CommandMorkatoBotException implements CommandException<MorkatoBotException> {
  @Override
  public void doException(TextCommandContext<?> ctx, MorkatoBotException exception) {
    ctx.send(exception.getMessage()).queue();
  }
}
