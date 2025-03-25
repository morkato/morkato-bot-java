package org.morkato.api.entity.impl.attack;

import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.attack.AttackFlags;
import org.morkato.api.entity.attack.AttackUpdateBuilder;
import org.morkato.api.repository.queries.attack.AttackUpdateQuery;
import java.math.BigDecimal;

public class ApiAttackUpdateBuilderImpl implements AttackUpdateBuilder{
  private final AttackUpdateQuery query = new AttackUpdateQuery();
  private final Attack attack;
  public ApiAttackUpdateBuilderImpl(Attack attack) {
    this.attack = attack;
  }

  @Override
  public AttackUpdateBuilder name(String value) {
    query.setName(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder description(String value) {
    query.setDescription(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder banner(String value) {
    query.setBanner(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder prefix(String value) {
    query.setPrefix(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder poisonTurn(BigDecimal value) {
    query.setPoisonTurn(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder poison(BigDecimal value) {
    query.setPoison(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder damage(BigDecimal value) {
    query.setDamage(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder breath(BigDecimal value) {
    query.setBreath(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder blood(BigDecimal value) {
    query.setBlood(value);
    return this;
  }

  @Override
  public AttackUpdateBuilder flags(AttackFlags flags) {
    query.setFlags(flags);
    return this;
  }

  @Override
  public Attack execute() {
    query
      .setGuildId(this.attack.getGuildId())
      .setGuildId(this.attack.getId());
    this.attack.update(query);
    return this.attack;
  }
}
