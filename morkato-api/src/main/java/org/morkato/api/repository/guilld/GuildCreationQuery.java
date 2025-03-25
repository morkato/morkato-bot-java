package org.morkato.api.repository.guilld;

import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.api.dto.GuildDTO;

import java.math.BigDecimal;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuildCreationQuery{
  private String id;
  private BigDecimal humanInitialLife;
  private BigDecimal oniInitialLife;
  private BigDecimal hybridInitialLife;
  private BigDecimal breathInitial;
  private BigDecimal bloodInitial;
  private BigDecimal abilityRoll;
  private BigDecimal familyRoll;

  public GuildCreationQuery validate(Validator validator) {
    GuildDTO dto = GuildDTO.from(this);
    dto.validateForCreate(validator);
    return this;
  }
}
