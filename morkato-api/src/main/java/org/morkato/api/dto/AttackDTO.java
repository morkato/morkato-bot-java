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
import org.morkato.api.repository.attack.AttackUpdateQuery;
import org.morkato.api.validation.constraints.*;
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
public final class AttackDTO extends DefaultDTO<AttackDTO> {
  @NotNull(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  private String guildId;
  @NotNull(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnId.class, OnCreate.class, OnUpdate.class})
  private String id;
  @NotNull(groups = {Default.class, OnCreate.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class})
  private String artId;
  @NotNull(groups = {Default.class, OnCreate.class})
  @MorkatoModelName(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String name;
  @MorkatoModelName(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String prefix;
  @NotNull(groups = Default.class)
  @MorkatoModelDescription(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String description;
  @MorkatoModelBanner(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String banner;
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal poisonTurn;
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal poison;
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal damage;
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal breath;
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal blood;
  private int flags;

  public static AttackDTO from(AttackUpdateQuery query) {
    throw new RuntimeException("Not implemented error");
  }

  @Override
  public Set<ConstraintViolation<AttackDTO>> safeValidate(Validator validator, Class<?>... classes) {
    return validator.validate(this, classes);
  }
}
