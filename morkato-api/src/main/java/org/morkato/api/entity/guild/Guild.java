package org.morkato.api.entity.guild;

import org.morkato.api.DeleteApiModel;
import org.morkato.api.entity.ObjectId;
import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;

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
  ObjectResolver<Art> getArtResolver();
}
