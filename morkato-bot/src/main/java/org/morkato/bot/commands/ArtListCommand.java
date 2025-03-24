package org.morkato.bot.commands;

import org.morkato.bmt.annotation.ApplicationProperty;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.NoArgs;

@Component
public class ArtListCommand implements Command<NoArgs> {
  @ApplicationProperty("morkato.property")
  private String property;

  @Override
  public void invoke(TextCommandContext<NoArgs> context) {
    context.sendMessage("Propriedade de aplicação injetada: " + property).queue();
  }

  @Override
  public SubCommand parent() {
    return new SubCommand("list", ArtCommand.class);
  }
}
