package org.morkato.bot.commands;

import org.morkato.bmt.NoArgs;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.TextCommandContext;

@Component
public class ArtListCommand implements Command<NoArgs> {
  @Override
  public void invoke(TextCommandContext<NoArgs> context) {
    context.sendMessage("EXECUTOU o subcomando list.").queue();
  }

  @Override
  public SubCommand parent() {
    return new SubCommand("list", ArtCommand.class);
  }
}
