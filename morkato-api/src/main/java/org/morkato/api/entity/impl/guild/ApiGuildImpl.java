package org.morkato.api.entity.impl.guild;

import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.GuildRepository;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.dto.GuildDTO;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Objects;

public class ApiGuildImpl implements Guild{
  private final RepositoryCentral central;
  private String id;
  private BigDecimal humanInitialLife;
  private BigDecimal oniInitialLife;
  private BigDecimal hybridInitialLife;
  private BigDecimal breathInitial;
  private BigDecimal bloodInitial;
  private BigDecimal abilityRoll;
  private BigDecimal familyRoll;
  public ApiGuildImpl(
    @Nonnull RepositoryCentral central,
    @Nonnull GuildDTO dto
  ) {
    this.central = central;
    this.fromDTO(dto);
  }

  private void fromDTO(GuildDTO dto) {
    this.id = dto.getId();
    this.humanInitialLife = dto.getHumanInitialLife();
    this.oniInitialLife = dto.getOniInitialLife();
    this.hybridInitialLife = dto.getHybridInitialLife();
    this.breathInitial = dto.getBreathInitial();
    this.bloodInitial = dto.getBloodInitial();
    this.abilityRoll = dto.getAbilityRoll();
    this.familyRoll = dto.getFamilyRoll();
  }

  @Override
  public String toString() {
    return Guild.representation(this);
  }

  @Nonnull
  @Override
  public String getId() {
    Objects.requireNonNull(this.id);
    return this.id;
  }

  @Nonnull
  @Override
  public BigDecimal getHumanInitialLife() {
    Objects.requireNonNull(this.humanInitialLife);
    return this.humanInitialLife;
  }

  @Nonnull
  @Override
  public BigDecimal getOniInitialLife() {
    Objects.requireNonNull(this.oniInitialLife);
    return this.oniInitialLife;
  }

  @Nonnull
  @Override
  public BigDecimal getHybridInitialLife() {
    Objects.requireNonNull(this.hybridInitialLife);
    return this.hybridInitialLife;
  }

  @Nonnull
  @Override
  public BigDecimal getBreathInitial() {
    Objects.requireNonNull(this.breathInitial);
    return this.breathInitial;
  }

  @Nonnull
  @Override
  public BigDecimal getBloodInitial() {
    Objects.requireNonNull(this.bloodInitial);
    return this.bloodInitial;
  }

  @Nonnull
  @Override
  public BigDecimal getAbilityRoll() {
    Objects.requireNonNull(this.abilityRoll);
    return this.abilityRoll;
  }

  @Nonnull
  @Override
  public BigDecimal getFamilyRoll() {
    Objects.requireNonNull(this.familyRoll);
    return this.familyRoll;
  }

  @Override
  public Guild delete() {
    GuildRepository guilds = central.guild();
    guilds.delete(this);
    return this;
  }
}
