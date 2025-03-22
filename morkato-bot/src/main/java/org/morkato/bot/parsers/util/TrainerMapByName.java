package org.morkato.bot.parsers.util;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.trainer.Trainer;

public class TrainerMapByName extends ObjectMapByName<Trainer> {
  @Override
  public ObjectResolver<Trainer> getResolver(Guild guild) {
    return guild.getTrainerResolver();
  }
}
