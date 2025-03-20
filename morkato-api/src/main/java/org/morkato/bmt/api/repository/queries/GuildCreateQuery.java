package org.morkato.bmt.api.repository.queries;

import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.bmt.api.dto.GuildDTO;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class GuildCreateQuery {
  private String id;
  private BigDecimal humanInitialLife;
  private BigDecimal oniInitialLife;
  private BigDecimal hybridInitialLife;
  private BigDecimal breathInitial;
  private BigDecimal bloodInitial;
  private BigDecimal abilityRoll;
  private BigDecimal familyRoll;

  public GuildCreateQuery validate(Validator validator) {
    GuildDTO dto = GuildDTO.from(this);
    dto.validateForCreate(validator);
    return this;
  }
}
