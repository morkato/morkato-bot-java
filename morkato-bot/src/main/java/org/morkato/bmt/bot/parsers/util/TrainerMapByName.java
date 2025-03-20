package org.morkato.bmt.bot.parsers.util;

import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.entity.trainer.Trainer;

public class TrainerMapByName extends ObjectMapByName<Trainer> {
  @Override
  public ObjectResolver<Trainer> getResolver(Guild guild) {
    return guild.getTrainerResolver();
  }
}
