package org.morkato.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.repository.queries.guild.GuildCreationQuery;
import org.morkato.api.repository.queries.guild.GuildIdQuery;
import org.morkato.api.validation.constraints.MorkatoModelAttribute;
import org.morkato.api.validation.constraints.MorkatoModelRoll;
import org.morkato.api.validation.constraints.MorkatoSnowflakeId;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnId;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuildDTO extends DefaultDTO<GuildDTO> {
  @NotNull(groups = {Default.class, OnCreate.class, OnId.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnId.class})
  private String id;
  @NotNull(groups = Default.class)
  @MorkatoModelAttribute(groups = Default.class)
  private BigDecimal humanInitialLife;
  @NotNull(groups = Default.class)
  @MorkatoModelAttribute(groups = Default.class)
  private BigDecimal oniInitialLife;
  @NotNull(groups = Default.class)
  @MorkatoModelAttribute(groups = Default.class)
  private BigDecimal hybridInitialLife;
  @NotNull(groups = Default.class)
  @MorkatoModelAttribute(groups = Default.class)
  private BigDecimal breathInitial;
  @NotNull(groups = Default.class)
  @MorkatoModelAttribute(groups = Default.class)
  private BigDecimal bloodInitial;
  @NotNull(groups = Default.class)
  @MorkatoModelRoll(groups = Default.class)
  private BigDecimal abilityRoll;
  @NotNull(groups = Default.class)
  @MorkatoModelRoll(groups = Default.class)
  private BigDecimal familyRoll;

  public static GuildDTO from(GuildId query) {
    return new GuildDTO()
      .setId(query.getId());
  }

  public static GuildDTO from(GuildCreationQuery query) {
    return new GuildDTO()
      .setId(query.getId())
      .setHumanInitialLife(query.getHumanInitialLife())
      .setOniInitialLife(query.getOniInitialLife())
      .setHybridInitialLife(query.getHybridInitialLife())
      .setBreathInitial(query.getBreathInitial())
      .setBloodInitial(query.getBloodInitial())
      .setAbilityRoll(query.getAbilityRoll())
      .setFamilyRoll(query.getFamilyRoll());
  }

  @Override
  public Set<ConstraintViolation<GuildDTO>> safeValidate(Validator validator, Class<?>... classes){
    return validator.validate(this, classes);
  }
}
