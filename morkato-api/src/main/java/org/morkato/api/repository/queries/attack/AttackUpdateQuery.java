package org.morkato.api.repository.queries.attack;

import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.api.dto.AttackDTO;
import org.morkato.api.entity.attack.AttackFlags;

import java.math.BigDecimal;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttackUpdateQuery {
  private String guildId;
  private String id;
  private String name;
  private String prefix;
  private String description;
  private String banner;
  private BigDecimal poisonTurn;
  private BigDecimal poison;
  private BigDecimal damage;
  private BigDecimal breath;
  private BigDecimal blood;
  private AttackFlags flags;

  public AttackUpdateQuery validate(Validator validator) {
    AttackDTO dto = AttackDTO.from(this);
    dto.validateForUpdate(validator);
    return this;
  }
}
