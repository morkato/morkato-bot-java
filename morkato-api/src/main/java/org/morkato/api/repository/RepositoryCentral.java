package org.morkato.api.repository;

import javax.annotation.Nonnull;

public interface RepositoryCentral {
  @Nonnull
  GuildRepository guild();
  @Nonnull
  AttackRepository attack();
  @Nonnull
  ArtRepository art();
  void setGuildRepository(@Nonnull GuildRepository repository);
  void setArtRepository(@Nonnull ArtRepository repository);
  void setAttackRepository(@Nonnull AttackRepository repository);
}
