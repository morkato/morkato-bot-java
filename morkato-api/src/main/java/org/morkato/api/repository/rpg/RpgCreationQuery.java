package org.morkato.api.repository.rpg;

import lombok.experimental.Accessors;
import lombok.Data;

import java.math.BigDecimal;

@Accessors(chain = true)
@Data
public class RpgCreationQuery {
  private BigDecimal humanInitialLife;
  private BigDecimal oniInitialLife;
  private BigDecimal hybridInitialLife;
  private BigDecimal breathInitial;
  private BigDecimal bloodInitial;
  private BigDecimal abilityRoll;
  private BigDecimal familyRoll;
}
