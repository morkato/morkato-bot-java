package org.morkato.api.repository.guild;

import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.Repository;
import org.morkato.api.entity.guild.GuildId;

import javax.annotation.Nonnull;

public interface GuildRepository extends Repository {
  @Nonnull
  GuildPayload fetch(GuildId query) throws RepositoryException;
  @Nonnull
  GuildPayload create(GuildCreationQuery query) throws RepositoryException;
  void delete(GuildId guild);
}
