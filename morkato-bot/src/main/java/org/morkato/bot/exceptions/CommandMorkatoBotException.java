package org.morkato.bot.exceptions;

import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.context.CommandContext;

public class CommandMorkatoBotException implements CommandException<MorkatoBotException> {
  @Override
  public void doException(CommandContext<?> ctx,MorkatoBotException exception) {
    ctx.sendMessage(exception.getMessage()).queue();
  }
}
