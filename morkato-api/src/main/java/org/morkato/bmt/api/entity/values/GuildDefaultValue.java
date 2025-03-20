package org.morkato.bmt.api.entity.values;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.bmt.api.dto.GuildDTO;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.repository.queries.GuildCreateQuery;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class GuildDefaultValue {
  private static final GuildDefaultValue values = new GuildDefaultValue();
  private BigDecimal humanInitialLife = new BigDecimal(1000).setScale(0, RoundingMode.HALF_UP);
  private BigDecimal oniInitialLife = new BigDecimal(500).setScale(0, RoundingMode.HALF_UP);
  private BigDecimal hybridInitialLife = new BigDecimal(1500 ).setScale(0, RoundingMode.HALF_UP);
  private BigDecimal breathInitial = new BigDecimal(500).setScale(0, RoundingMode.HALF_UP);
  private BigDecimal bloodInitial = new BigDecimal(1000).setScale(0, RoundingMode.HALF_UP);
  private BigDecimal abilityRoll = new BigDecimal(3).setScale(0, RoundingMode.HALF_UP);
  private BigDecimal familyRoll = new BigDecimal(3).setScale(0, RoundingMode.HALF_UP);

  public static GuildDefaultValue getDefault() {
    return values;
  }

  @Nonnull
  public static GuildDefaultValue from(@Nonnull GuildCreateQuery query) {
    Objects.requireNonNull(query);
    final GuildDefaultValue values = new GuildDefaultValue();
    if (Objects.nonNull(query.getHumanInitialLife()))
      values.setHumanInitialLife(query.getHumanInitialLife());
    if (Objects.nonNull(query.getOniInitialLife()))
      values.setOniInitialLife(query.getOniInitialLife());
    if (Objects.nonNull(query.getHybridInitialLife()))
      values.setHybridInitialLife(query.getHybridInitialLife());
    if (Objects.nonNull(query.getBreathInitial()))
      values.setBreathInitial(query.getBreathInitial());
    if (Objects.nonNull(query.getBloodInitial()))
      values.setBloodInitial(query.getBloodInitial());
    if (Objects.nonNull(query.getAbilityRoll()))
      values.setAbilityRoll(query.getAbilityRoll());
    if (Objects.nonNull(query.getFamilyRoll()))
      values.setFamilyRoll(query.getFamilyRoll());
    return values;
  }

  public GuildDTO getDTO() {
    return new GuildDTO()
      .setHumanInitialLife(this.getHumanInitialLife())
      .setOniInitialLife(this.getHumanInitialLife())
      .setHybridInitialLife(this.getHybridInitialLife())
      .setBreathInitial(this.getBreathInitial())
      .setBloodInitial(this.getBloodInitial())
      .setAbilityRoll(this.getAbilityRoll())
      .setFamilyRoll(this.getFamilyRoll());
  }

  public boolean isHumanInitialLife(Guild guild) {
    return guild.getHumanInitialLife().equals(this.getHumanInitialLife());
  }

  public boolean isOniInitialLife(Guild guild) {
    return guild.getOniInitialLife().equals(this.getOniInitialLife());
  }

  public boolean isHybridInitialLife(Guild guild) {
    return guild.getHybridInitialLife().equals(this.getHybridInitialLife());
  }

  public boolean isBreathInitial(Guild guild) {
    return guild.getBreathInitial().equals(this.getBreathInitial());
  }

  public boolean isBloodInitial(Guild guild) {
    return guild.getBloodInitial().equals(this.getBloodInitial());
  }

  public boolean isAbilityRoll(Guild guild) {
    return guild.getAbilityRoll().equals(this.getAbilityRoll());
  }

  public boolean isFamilyRoll(Guild guild) {
    return guild.getFamilyRoll().equals(this.getFamilyRoll());
  }
}