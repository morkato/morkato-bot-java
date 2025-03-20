package org.morkato.bmt.bot.commands;

import org.morkato.bmt.api.repository.RepositoryCentral;
import org.morkato.bmt.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bot.parameters.ArtCommandData;

@MorkatoComponent
public class ArtCommand implements Command<ArtCommandData> {
  @AutoInject
  RepositoryCentral central;
  @Override
  public void invoke(TextCommandContext<ArtCommandData> ctx) {
    ctx.sendMessage("" + ctx.getArgs()).queue();
  }
}
