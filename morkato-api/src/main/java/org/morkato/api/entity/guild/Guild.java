package org.morkato.api.entity.guild;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.rpg.Rpg;
import org.morkato.api.entity.trainer.Trainer;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.DeleteApiModel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface Guild
  extends DeleteApiModel<Guild>,
          GuildPayload,
          GuildId {
  static String representation(Guild guild) {
    return "Guild[id = " + guild.getId() + "]";
  }
  ObjectResolver<Attack> getAttackResolver();
  ObjectResolver<Trainer> getTrainerResolver();
  ObjectResolver<Art> getArtResolver();
  Rpg getRpg();
}
