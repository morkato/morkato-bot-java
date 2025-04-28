package org.morkato.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.groups.Default;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.morkato.api.entity.MorkatoModelType;
import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.repository.guild.GuildCreationQuery;
import org.morkato.api.validation.constraints.Mcisidv1Id;
import org.morkato.api.validation.constraints.MorkatoModelAttribute;
import org.morkato.api.validation.constraints.MorkatoSnowflakeId;
import org.morkato.api.validation.constraints.MorkatoModelRoll;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnId;
import org.morkato.api.entity.guild.GuildId;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import org.morkato.api.validation.groups.OnUpdate;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuildDTO extends DefaultDTO<GuildDTO> implements GuildPayload {
  private static final GuildDTO defaultValue = new GuildDTO();

  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnId.class})
  @NotNull(groups = {Default.class, OnCreate.class, OnId.class})
  private String id;
  @Mcisidv1Id(model = MorkatoModelType.RPG, groups = {Default.class, OnCreate.class})
  @NotNull(groups = {Default.class, OnCreate.class})
  private String rpgId;
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String rollCategoryId;
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String offCategoryId;

  public static GuildDTO from(GuildId query) {
    return new GuildDTO()
      .setId(query.getId());
  }

  public static GuildDTO from(GuildCreationQuery query) {
    return new GuildDTO()
      .setId(query.getId())
      .setRpgId(query.getRpgId())
      .setRollCategoryId(query.getRollCategoryId())
      .setOffCategoryId(query.getOffCategoryId());
  }

  public static GuildDTO normalizeDefault(GuildCreationQuery query) {
    return defaultValue.normalize(query);
  }

  public GuildDTO normalize(GuildCreationQuery query) {
    return new GuildDTO()
      .setId(query.getId())
      .setRpgId(query.getRpgId())
      .setRollCategoryId(Objects.isNull(query.getRollCategoryId()) ? this.getRollCategoryId() : query.getRollCategoryId())
      .setOffCategoryId(Objects.isNull(query.getOffCategoryId()) ? this.getOffCategoryId() : query.getOffCategoryId());
  }

  @Override
  public Set<ConstraintViolation<GuildDTO>> safeValidate(Validator validator, Class<?>... classes){
    return validator.validate(this, classes);
  }
}
