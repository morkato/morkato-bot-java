package org.morkato.api.entity.guild;

import org.morkato.api.DeleteApiModel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface Guild extends DeleteApiModel<Guild> {
  static String representation(Guild guild) {
    return "Guild[id = " + guild.getId() + "]";
  }
  @Nonnull
  String getId();
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
}
