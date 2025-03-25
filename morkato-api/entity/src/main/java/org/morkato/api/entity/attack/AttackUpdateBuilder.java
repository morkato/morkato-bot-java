package org.morkato.api.entity.attack;

import org.morkato.api.entity.DataBuilder;
import java.math.BigDecimal;

public interface AttackUpdateBuilder extends DataBuilder<Attack> {
  AttackUpdateBuilder name(String value);
  AttackUpdateBuilder description(String value);
  AttackUpdateBuilder banner(String value);
  AttackUpdateBuilder prefix(String value);
  AttackUpdateBuilder poisonTurn(BigDecimal value);
  AttackUpdateBuilder poison(BigDecimal value);
  AttackUpdateBuilder damage(BigDecimal value);
  AttackUpdateBuilder breath(BigDecimal value);
  AttackUpdateBuilder blood(BigDecimal value);
  AttackUpdateBuilder flags(AttackFlags flags);
}
