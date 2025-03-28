package org.morkato.api.dto;

import org.morkato.api.validation.constraints.MorkatoModelRoll;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import org.morkato.api.entity.user.UserType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnUpdate;
import org.morkato.api.validation.groups.OnId;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, of = {"guildId","id"})
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO extends DefaultDTO<UserDTO> {
  @NotNull(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  private String guildId;
  @NotNull(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  private String id;
  @NotNull(groups = {Default.class, OnCreate.class})
  private UserType type;
  @NotNull(groups = Default.class)
  private Integer flags;
  @NotNull(groups = Default.class)
  @MorkatoModelRoll(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal abilityRoll;
  @NotNull(groups = Default.class)
  @MorkatoModelRoll(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal familyRoll;
  @NotNull(groups = Default.class)
  @MorkatoModelRoll(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal prodigyRoll;
  @NotNull(groups = Default.class)
  @MorkatoModelRoll(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal markRoll;
  @NotNull(groups = Default.class)
  @MorkatoModelRoll(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal berserkRoll;

  @Override
  public Set<ConstraintViolation<UserDTO>> safeValidate(Validator validator, Class<?>... classes) {
    return validator.validate(this, classes);
  }
}
