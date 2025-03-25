package org.morkato.api.entity;

import org.morkato.api.repository.attack.AttackUpdateQuery;

public interface UpdateApiModel<M, T extends DataBuilder<M>> {
  T doUpdate();
  void update(AttackUpdateQuery query);
}
