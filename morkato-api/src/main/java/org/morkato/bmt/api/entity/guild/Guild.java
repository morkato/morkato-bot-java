package org.morkato.bmt.api.entity.guild;

import org.morkato.bmt.api.DeleteApiModel;
import org.morkato.bmt.api.entity.ObjectId;
import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.art.Art;
import org.morkato.bmt.api.entity.trainer.Trainer;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface Guild extends ObjectId, DeleteApiModel<Guild> {
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
