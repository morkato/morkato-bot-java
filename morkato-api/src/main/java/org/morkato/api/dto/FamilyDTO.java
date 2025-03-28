package org.morkato.api.dto;

import org.morkato.api.validation.constraints.*;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnUpdate;
import org.morkato.api.validation.groups.OnId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import jakarta.validation.Validator;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, of = {"guildId","id"})
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FamilyDTO extends DefaultDTO<FamilyDTO> {
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
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal percent;
  @NotNull(groups = {Default.class, OnCreate.class})
  private Integer userType;
  @MorkatoModelDescription(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String description;
  @MorkatoModelBanner(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String banner;

  @Override
  public Set<ConstraintViolation<FamilyDTO>> safeValidate(Validator validator, Class<?>... classes) {
    return validator.validate(this, classes);
  }
}
