package org.morkato.api.repository;

import javax.annotation.Nonnull;

public interface RepositoryCentral {
  @Nonnull
  GuildRepository guild();
  @Nonnull
  AttackRepository attack();
  @Nonnull
  ArtRepository art();
}
