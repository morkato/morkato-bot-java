package org.morkato.bot.exceptions;

import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.context.TextCommandContext;

@Component
public class CommandMorkatoBotException implements CommandException<MorkatoBotException> {
  @Override
  public void doException(TextCommandContext<?> ctx, MorkatoBotException exception) {
    ctx.sendMessage(exception.getMessage()).queue();
  }
}
