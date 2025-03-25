package org.morkato.bot.parsers;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.trainer.Trainer;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.repository.guilld.GuildIdQuery;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bot.parsers.util.TrainerMapByName;

import java.util.Objects;

@Component
public class TrainerParser implements ObjectParser<Trainer> {
  private TrainerMapByName cachedTrainers = new TrainerMapByName();
  @AutoInject
  public RepositoryCentral central;
  @Override
  public Trainer parse(TextCommandContext<?> ctx, String text) {
    String guildId = ctx.getGuild().getId();
    Guild guild = central.fetchGuild(new GuildIdQuery(guildId));
    ObjectResolver<Trainer> trainers = guild.getTrainerResolver();
    trainers.resolve();
    Trainer trainer = cachedTrainers.get(guild, text);
    if (Objects.isNull(trainer))
      throw new RuntimeException("O treino chamado: **" + text + "** não existe.");
    return trainer;
  }
}
