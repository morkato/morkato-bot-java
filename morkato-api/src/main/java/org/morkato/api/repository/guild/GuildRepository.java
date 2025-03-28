package org.morkato.api.repository.guild;

import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.exception.InvalidEntityQuery;
import org.morkato.api.exception.guild.GuildAlreadyExistsError;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.exception.repository.RepositoryInternalError;
import org.morkato.api.repository.Repository;
import org.morkato.api.exception.guild.GuildIdInvalidError;
import org.morkato.api.exception.guild.GuildNotFoundError;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.dto.GuildDTO;
import javax.annotation.Nonnull;

public interface GuildRepository extends Repository{
  @Nonnull
  GuildPayload fetch(@Nonnull GuildId query) throws RepositoryException;
  @Nonnull
  GuildPayload fetch(@Nonnull String id) throws RepositoryException;
  @Nonnull
  GuildPayload create(@Nonnull GuildCreationQuery query) throws RepositoryException;
  void delete(@Nonnull GuildId guild);
}
