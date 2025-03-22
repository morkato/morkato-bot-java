package org.morkato.api.repository;

import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.entity.guild.Guild;
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
  @Nonnull
  Guild fetchGuild(GuildId id);
  Guild getCachedGuild(GuildId id);
}
