package org.morkato.bmt.api.repository.queries;


import jakarta.validation.Validator;
import org.morkato.bmt.api.dto.GuildDTO;

public record GuildIdQuery(
  String id
) {
  public GuildIdQuery validate(Validator validator) {
    GuildDTO dto = GuildDTO.from(this);
    dto.validateForId(validator);
    return this;
  }
}
