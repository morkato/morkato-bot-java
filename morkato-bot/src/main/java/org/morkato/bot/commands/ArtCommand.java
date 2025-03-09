package org.morkato.bot.commands;

import org.morkato.api.repository.RepositoryCentral;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.components.Command;
import org.morkato.api.entity.art.Art;
import org.morkato.bmt.context.TextCommandContext;

@MorkatoComponent
public class ArtCommand implements Command<Art> {
  @AutoInject
  RepositoryCentral central;
  @Override
  public void invoke(TextCommandContext<Art> ctx) throws Throwable {
    ctx.sendMessage("" + ctx.getArgs()).queue();
  }
}
