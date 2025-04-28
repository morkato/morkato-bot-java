package org.morkato.api.entity.rpg;

import java.math.BigDecimal;

public interface RpgPayload extends RpgId {
  BigDecimal getHumanInitialLife();
  BigDecimal getOniInitialLife();
  BigDecimal getHybridInitialLife();
  BigDecimal getBreathInitial();
  BigDecimal getBloodInitial();
  BigDecimal getAbilityRoll();
  BigDecimal getFamilyRoll();
}
