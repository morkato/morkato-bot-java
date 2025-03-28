package org.morkato.api.entity.guild;

import org.morkato.api.dto.GuildDTO;
import org.morkato.api.entity.impl.guild.GuildPayloadImpl;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface GuildPayload extends GuildId {
  @Nonnull
  static GuildPayload create(GuildDTO dto) {
    return new GuildPayloadImpl(dto);
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
}
