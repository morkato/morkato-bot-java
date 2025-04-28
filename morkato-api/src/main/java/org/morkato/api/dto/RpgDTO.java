package org.morkato.api.dto;

import org.morkato.api.entity.MorkatoModelType;
import org.morkato.api.entity.rpg.RpgPayload;
import org.morkato.api.repository.rpg.RpgCreationQuery;
import org.morkato.api.validation.constraints.Mcisidv1Id;
import org.morkato.api.validation.constraints.MorkatoModelAttribute;
import org.morkato.api.validation.constraints.MorkatoModelRoll;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.groups.Default;
import jakarta.validation.Validator;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, of = "id")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RpgDTO extends DefaultDTO<RpgDTO> implements RpgPayload {
  private static final RpgDTO defaultValue = new RpgDTO()
    .setHumanInitialLife(BigDecimal.valueOf(1000).setScale(0, RoundingMode.HALF_UP))
    .setOniInitialLife(BigDecimal.valueOf(500).setScale(0, RoundingMode.HALF_UP))
    .setHybridInitialLife(BigDecimal.valueOf(1500).setScale(0, RoundingMode.HALF_UP))
    .setBreathInitial(BigDecimal.valueOf(500).setScale(0, RoundingMode.HALF_UP))
    .setBloodInitial(BigDecimal.valueOf(1000).setScale(0, RoundingMode.HALF_UP))
    .setAbilityRoll(BigDecimal.valueOf(3).setScale(0, RoundingMode.HALF_UP))
    .setFamilyRoll(BigDecimal.valueOf(3).setScale(0, RoundingMode.HALF_UP));
  @NotNull(groups = {Default.class, OnCreate.class, OnId.class})
  @Mcisidv1Id(model = MorkatoModelType.RPG, groups = {Default.class, OnCreate.class, OnId.class})
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

  public static RpgDTO normalizeDefault(RpgCreationQuery query) {
    return defaultValue.normalize(query);
  }

  public RpgDTO normalize(RpgCreationQuery query) {
    return new RpgDTO()
      .setId(this.getId())
      .setHumanInitialLife(Objects.isNull(query.getHumanInitialLife()) ? this.getHumanInitialLife() : query.getHumanInitialLife())
      .setOniInitialLife(Objects.isNull(query.getOniInitialLife()) ? this.getOniInitialLife() : query.getOniInitialLife())
      .setHybridInitialLife(Objects.isNull(query.getHybridInitialLife()) ? this.getHybridInitialLife() : query.getHybridInitialLife())
      .setBreathInitial(Objects.isNull(query.getBreathInitial()) ? this.getBreathInitial() : query.getBreathInitial())
      .setBloodInitial(Objects.isNull(query.getBloodInitial()) ? this.getBloodInitial() : query.getBloodInitial())
      .setAbilityRoll(Objects.isNull(query.getAbilityRoll()) ? this.getAbilityRoll() : query.getAbilityRoll())
      .setFamilyRoll(Objects.isNull(query.getFamilyRoll()) ? this.getFamilyRoll() : query.getFamilyRoll());
  }

  @Override
  public Set<ConstraintViolation<RpgDTO>> safeValidate(Validator validator, Class<?>... classes){
    return validator.validate(this, classes);
  }
}
