package org.morkato.api.repository.art;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.morkato.api.dto.ArtDTO;
import org.morkato.api.entity.art.ArtId;
import org.morkato.api.entity.art.ArtType;

import javax.annotation.Nonnull;

public record ArtUpdateQuery(
  String guildId,
  String id,
  String name,
  ArtType type,
  String description,
  String banner
) implements ArtId {
  public ArtUpdateQuery validate(Validator validator) throws ConstraintViolationException {
    ArtDTO dto = ArtDTO.from(this);
    dto.validateForUpdate(validator);
    return this;
  }

  @Override
  @Nonnull
  public String getGuildId() {
    return this.guildId();
  }

  @Override
  @Nonnull
  public String getId() {
    return this.id();
  }
}
