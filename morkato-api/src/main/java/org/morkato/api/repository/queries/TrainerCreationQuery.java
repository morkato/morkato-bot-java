package org.morkato.api.repository.queries;

import org.morkato.api.dto.TrainerDTO;
import jakarta.validation.Validator;

import java.math.BigDecimal;

public record TrainerCreationQuery(
  String guildId,
  String name,
  long cooldown,
  BigDecimal life,
  BigDecimal breath,
  BigDecimal blood,
  String emoji,
  String banner,
  long xp
) {
  public TrainerCreationQuery validate(Validator validator) {
    TrainerDTO dto = TrainerDTO.from(this);
    dto.validateForCreate(validator);
    return this;
  }
}
