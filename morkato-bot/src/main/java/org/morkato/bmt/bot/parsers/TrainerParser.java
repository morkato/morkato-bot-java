package org.morkato.bmt.bot.parsers;

import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.entity.trainer.Trainer;
import org.morkato.bmt.api.repository.RepositoryCentral;
import org.morkato.bmt.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.components.ObjectParser;
import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bot.parsers.util.TrainerMapByName;

import java.util.Objects;

@MorkatoComponent
public class TrainerParser implements ObjectParser<Trainer> {
  private TrainerMapByName cachedTrainers = new TrainerMapByName();
  @AutoInject
  public RepositoryCentral central;
  @Override
  public Trainer parse(TextCommandContext<?> ctx, String text) {
    String guildId = ctx.getGuild().getId();
    Guild guild = central.guild().fetch(guildId);
    ObjectResolver<Trainer> trainers = guild.getTrainerResolver();
    trainers.resolve();
    Trainer trainer = cachedTrainers.get(guild, text);
    if (Objects.isNull(trainer))
      throw new RuntimeException("O treino chamado: **" + text + "** não existe.");
    return trainer;
  }
}
