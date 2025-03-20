package org.morkato.bmt.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.bmt.api.entity.art.Art;
import org.morkato.bmt.api.repository.queries.ArtCreationQuery;
import org.morkato.api.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import org.morkato.bmt.api.entity.art.ArtType;
import org.morkato.bmt.api.validation.constraints.MorkatoModelBanner;
import org.morkato.bmt.api.validation.constraints.MorkatoModelDescription;
import org.morkato.bmt.api.validation.constraints.MorkatoModelName;
import org.morkato.bmt.api.validation.constraints.MorkatoSnowflakeId;
import org.morkato.bmt.api.validation.groups.OnCreate;
import org.morkato.bmt.api.validation.groups.OnUpdate;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, of = {"guildId","id"})
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class ArtDTO extends DefaultDTO<ArtDTO> {
  @NotNull(groups = {Default.class, OnCreate.class, OnUpdate.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String guildId;
  @NotNull(groups = {Default.class, OnCreate.class, OnUpdate.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String id;
  @NotNull(groups = {Default.class, OnCreate.class})
  @MorkatoModelName(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String name;
  @NotNull(groups = {Default.class, OnCreate.class})
  private ArtType type;
  @MorkatoModelDescription(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String description;
  @MorkatoModelBanner(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String banner;

  public static ArtDTO fromArt(Art art) {
    return new ArtDTO(
      art.getGuildId(),
      art.getId(),
      art.getName(),
      art.getType(),
      art.getDescription(),
      art.getBanner()
    );
  }

  public static ArtDTO from(ArtCreationQuery query) {
    return new ArtDTO(
      query.guildId(), null, query.name(),
      query.type(), query.description(), query.banner()
    );
  }


  @Override
  public Set<ConstraintViolation<ArtDTO>> safeValidate(Validator validator, Class<?>... classes) {
    return validator.validate(this, classes);
  }
}
