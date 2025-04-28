package org.morkato.api;

import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.entity.rpg.Rpg;
import org.morkato.api.entity.rpg.RpgId;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.rpg.RpgCreationQuery;

import javax.annotation.Nonnull;

public interface ApiConnectionStatement {
  @Nonnull
  Guild fetchGuild(GuildId query) throws RepositoryException;
  @Nonnull
  Guild createGuild(GuildId query) throws RepositoryException;
  @Nonnull
  Guild fetchOrCreateGuild(GuildId query) throws RepositoryException;
  void deleteGuild(GuildId query) throws RepositoryException;
  @Nonnull
  Rpg fetchRpg(RpgId query) throws RepositoryException;
  @Nonnull
  Rpg createRpg(RpgCreationQuery query) throws RepositoryException;;
  void deleteRpg(RpgId query) throws RepositoryException;
}
