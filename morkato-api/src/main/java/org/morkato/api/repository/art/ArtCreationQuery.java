package org.morkato.api.repository.art;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.api.entity.art.ArtType;
import jakarta.validation.Validator;
import org.morkato.api.dto.ArtDTO;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class ArtCreationQuery {
  private String guildId;
  private String name;
  private ArtType type;
  private String description;
  private String banner;

  public ArtCreationQuery validate(Validator validator) throws ConstraintViolationException {
    ArtDTO dto = ArtDTO.from(this);
    dto.validateForCreate(validator);
    return this;
  }
}
