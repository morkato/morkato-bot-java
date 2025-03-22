package org.morkato.api.entity.guild;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.trainer.Trainer;
import org.morkato.api.entity.ObjectId;
import org.morkato.api.entity.art.Art;
import org.morkato.api.DeleteApiModel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface Guild extends GuildId, DeleteApiModel<Guild> {
  static String representation(Guild guild) {
    return "Guild[id = " + guild.getId() + "]";
  }
  @Nonnull
  BigDecimal getHumanInitialLife();
  @Nonnull
  BigDecimal getOniInitialLife();
  @Nonnull
  BigDecimal getHybridInitialLife();
  @Nonnull
  BigDecimal getBreathInitial();
  @Nonnull
  BigDecimal getBloodInitial();
  @Nonnull
  BigDecimal getAbilityRoll();
  @Nonnull
  BigDecimal getFamilyRoll();
  ObjectResolver<Trainer> getTrainerResolver();
  ObjectResolver<Art> getArtResolver();
}
