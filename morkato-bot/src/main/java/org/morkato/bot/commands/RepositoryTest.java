package org.morkato.bot.commands;

import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.argument.NoArgs;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.api.repository.ArtRepository;
import org.morkato.api.repository.RepositoryCentral;

@MorkatoComponent(extension = org.morkato.bot.extension.MorkatoAPIExtension.class)
public class RepositoryTest implements Command<NoArgs> {
  @AutoInject
  RepositoryCentral repository;
  @Override
  public void invoke(TextCommandContext<NoArgs> ctx) throws Throwable {
    ctx.send("Repositório injetado: **" + repository + "** diretamente de: **" + ArtRepository.class.getName()+ "**.").queue();
  }
}
