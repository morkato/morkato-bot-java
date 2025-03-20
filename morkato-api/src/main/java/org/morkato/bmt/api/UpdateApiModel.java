package org.morkato.bmt.api;

import org.morkato.bmt.api.repository.queries.attack.AttackUpdateQuery;

public interface UpdateApiModel<M, T extends DataBuilder<M>> {
  T doUpdate();
  void update(AttackUpdateQuery query);
}
