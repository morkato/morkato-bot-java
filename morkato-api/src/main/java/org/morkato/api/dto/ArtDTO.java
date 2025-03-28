package org.morkato.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.groups.Default;
import jakarta.validation.Validator;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.morkato.api.repository.art.ArtUpdateQuery;
import org.morkato.api.validation.constraints.MorkatoModelBanner;
import org.morkato.api.validation.constraints.MorkatoModelDescription;
import org.morkato.api.validation.constraints.MorkatoModelName;
import org.morkato.api.validation.constraints.MorkatoSnowflakeId;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.repository.art.ArtCreationQuery;
import org.morkato.api.validation.groups.OnId;
import org.morkato.api.validation.groups.OnUpdate;
import org.morkato.api.entity.art.ArtType;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, of = {"guildId","id"})
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class ArtDTO extends DefaultDTO<ArtDTO> {
  @NotNull(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  private String guildId;
  @NotNull(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
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

  public static ArtDTO from(ArtCreationQuery query) {
    return new ArtDTO(
      query.guildId(), null, query.name(),
      query.type(), query.description(), query.banner()
    );
  }

  public static ArtDTO from(ArtUpdateQuery query) {
    return new ArtDTO()
      .setName(query.name())
      .setType(query.type())
      .setDescription(query.description())
      .setBanner(query.banner());
  }

  @Override
  public Set<ConstraintViolation<ArtDTO>> safeValidate(Validator validator, Class<?>... classes) {
    return validator.validate(this, classes);
  }
}
