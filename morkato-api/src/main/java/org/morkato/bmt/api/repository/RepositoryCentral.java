package org.morkato.bmt.api.repository;

import javax.annotation.Nonnull;

public interface RepositoryCentral {
  @Nonnull
  GuildRepository guild();
  @Nonnull
  TrainerRepository trainer();
  @Nonnull
  AttackRepository attack();
  @Nonnull
  ArtRepository art();
}
