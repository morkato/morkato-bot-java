package org.morkato.api.repository.art;

import jakarta.validation.ConstraintViolationException;
import org.morkato.api.entity.art.ArtType;
import jakarta.validation.Validator;
import org.morkato.api.dto.ArtDTO;

import javax.annotation.Nonnull;
import java.util.Objects;

public record ArtCreationQuery(
  String guildId,
  String name,
  ArtType type,
  String description,
  String banner
) {
  public static ArtCreationQuery from(@Nonnull ArtDTO dto, @Nonnull Validator validator) {
    Objects.requireNonNull(dto);
    Objects.requireNonNull(validator);
    return new ArtCreationQuery(
      dto.getGuildId(),
      dto.getName(),
      dto.getType(),
      dto.getDescription(),
      dto.getBanner()
    ).validate(validator);
  }

  public ArtCreationQuery validate(Validator validator) throws ConstraintViolationException {
    ArtDTO dto = ArtDTO.from(this);
    dto.validateForCreate(validator);
    return this;
  }
}
