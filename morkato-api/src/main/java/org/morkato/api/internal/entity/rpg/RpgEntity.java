package org.morkato.api.internal.entity.rpg;

import org.jetbrains.annotations.NotNull;
import org.morkato.api.ApiConnectionStatement;
import org.morkato.api.entity.rpg.Rpg;
import org.morkato.api.entity.rpg.RpgPayload;
import org.morkato.api.entity.rpg.RpgUpdateBuilder;
import org.morkato.api.repository.attack.AttackUpdateQuery;
import org.morkato.api.repository.rpg.RpgRepository;

import java.math.BigDecimal;
import java.util.Objects;

public class RpgEntity implements Rpg {
  private final ApiConnectionStatement statement;
  private final String id;
  private BigDecimal humanInitialLife;
  private BigDecimal oniInitialLife;
  private BigDecimal hybridInitialLife;
  private BigDecimal breathInitial;
  private BigDecimal bloodInitial;
  private BigDecimal abilityRoll;
  private BigDecimal familyRoll;

  public RpgEntity(ApiConnectionStatement statement, RpgPayload payload) {
    this.statement = Objects.requireNonNull(statement);
    this.id = Objects.requireNonNull(payload.getId());
    this.fromPayload(payload);
  }

  private void fromPayload(RpgPayload payload) {
    this.humanInitialLife = Objects.requireNonNull(payload.getHumanInitialLife());
    this.oniInitialLife = Objects.requireNonNull(payload.getOniInitialLife());
    this.hybridInitialLife = Objects.requireNonNull(payload.getHybridInitialLife());
    this.breathInitial = Objects.requireNonNull(payload.getBreathInitial());
    this.bloodInitial = Objects.requireNonNull(payload.getBloodInitial());
    this.abilityRoll = Objects.requireNonNull(payload.getAbilityRoll());
    this.familyRoll = Objects.requireNonNull(payload.getFamilyRoll());
  }

  @NotNull
  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public BigDecimal getHumanInitialLife() {
    return this.humanInitialLife;
  }

  @Override
  public BigDecimal getOniInitialLife() {
    return this.oniInitialLife;
  }

  @Override
  public BigDecimal getHybridInitialLife() {
    return this.hybridInitialLife;
  }

  @Override
  public BigDecimal getBreathInitial() {
    return this.breathInitial;
  }

  @Override
  public BigDecimal getBloodInitial() {
    return this.bloodInitial;
  }

  @Override
  public BigDecimal getAbilityRoll() {
    return this.abilityRoll;
  }

  @Override
  public BigDecimal getFamilyRoll() {
    return this.familyRoll;
  }

  @Override
  public RpgUpdateBuilder doUpdate() {
    throw new RuntimeException("Not yet implemented.");
  }

  @Override
  public void update(AttackUpdateQuery query) {
    throw new RuntimeException("Not yet implemented.");
  }

  @Override
  public Rpg delete() {
    statement.deleteRpg(this);
    return this;
  }
}
