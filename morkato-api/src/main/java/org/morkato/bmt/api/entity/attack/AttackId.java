package org.morkato.bmt.api.entity.attack;

import javax.annotation.Nonnull;

public interface AttackId {
  @Nonnull
  String getGuildId();
  @Nonnull
  String getId();
}
