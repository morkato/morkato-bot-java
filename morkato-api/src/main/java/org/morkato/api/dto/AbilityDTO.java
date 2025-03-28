package org.morkato.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.morkato.api.validation.constraints.MorkatoModelDescription;
import org.morkato.api.validation.constraints.MorkatoSnowflakeId;
import org.morkato.api.validation.constraints.MorkatoModelBanner;
import org.morkato.api.validation.constraints.MorkatoModelName;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnId;
import org.morkato.api.validation.groups.OnUpdate;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, of = {"guildId","id"})
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AbilityDTO extends DefaultDTO<AbilityDTO> {
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
  private BigDecimal percent;
  @NotNull(groups = {Default.class, OnCreate.class})
  private int userType;
  @MorkatoModelDescription(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String description;
  @MorkatoModelBanner(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String banner;

  @Override
  public Set<ConstraintViolation<AbilityDTO>> safeValidate(Validator validator, Class<?>... classes) {
    return validator.validate(this, classes);
  }
}
