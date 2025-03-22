package org.morkato.api;

import org.morkato.api.repository.queries.attack.AttackUpdateQuery;

public interface UpdateApiModel<M, T extends DataBuilder<M>> {
  T doUpdate();
  void update(AttackUpdateQuery query);
}
