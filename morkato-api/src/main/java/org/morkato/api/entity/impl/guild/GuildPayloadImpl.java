package org.morkato.api.entity.impl.guild;

import org.morkato.api.dto.GuildDTO;
import org.morkato.api.entity.guild.GuildPayload;
import lombok.Getter;
import org.morkato.api.values.GuildDefaultValue;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class GuildPayloadImpl implements GuildPayload {
  private static final GuildDefaultValue VALUES = GuildDefaultValue.getDefault();
  private final String id;
  private final BigDecimal humanInitialLife;
  private final BigDecimal oniInitialLife;
  private final BigDecimal hybridInitialLife;
  private final BigDecimal breathInitial;
  private final BigDecimal bloodInitial;
  private final BigDecimal abilityRoll;
  private final BigDecimal familyRoll;

  public GuildPayloadImpl(
    @Nonnull String id,
    @Nonnull BigDecimal humanInitialLife,
    @Nonnull BigDecimal oniInitialLife,
    @Nonnull BigDecimal hybridInitialLife,
    @Nonnull BigDecimal breathInitial,
    @Nonnull BigDecimal bloodInitial,
    @Nonnull BigDecimal abilityRoll,
    @Nonnull BigDecimal familyRoll
  ) {
    this.id = Objects.requireNonNull(id);
    this.humanInitialLife = Objects.requireNonNull(humanInitialLife);
    this.oniInitialLife = Objects.requireNonNull(oniInitialLife);
    this.hybridInitialLife = Objects.requireNonNull(hybridInitialLife);
    this.breathInitial = Objects.requireNonNull(breathInitial);
    this.bloodInitial = Objects.requireNonNull(bloodInitial);
    this.abilityRoll = Objects.requireNonNull(abilityRoll);
    this.familyRoll = Objects.requireNonNull(familyRoll);
  }

  public GuildPayloadImpl(GuildDTO dto) {
    this(
      dto.getId(),
      dto.getHumanInitialLife(),
      dto.getOniInitialLife(),
      dto.getHybridInitialLife(),
      dto.getBreathInitial(),
      dto.getBloodInitial(),
      dto.getAbilityRoll(),
      dto.getFamilyRoll()
    );
  }
}
