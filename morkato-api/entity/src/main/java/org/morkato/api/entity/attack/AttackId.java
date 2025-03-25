package org.morkato.api.entity.attack;

import javax.annotation.Nonnull;

public interface AttackId {
  @Nonnull
  String getGuildId();
  @Nonnull
  String getId();
}
