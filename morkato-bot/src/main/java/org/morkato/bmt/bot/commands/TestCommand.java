package org.morkato.bmt.bot.commands;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.argument.NoArgs;

@MorkatoComponent
public class TestCommand implements Command<NoArgs> {
  @Override
  public void invoke(TextCommandContext<NoArgs> context) throws Throwable {
    context.sendMessage("respondi").queue();
    Thread.sleep(15000);
  }
}
