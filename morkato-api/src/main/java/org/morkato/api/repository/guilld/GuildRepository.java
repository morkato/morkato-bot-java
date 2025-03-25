package org.morkato.api.repository.guilld;

import org.morkato.api.exception.repository.RepositoryInternalError;
import org.morkato.api.repository.Repository;
import org.morkato.api.exception.guild.GuildIdInvalidError;
import org.morkato.api.exception.guild.GuildNotFoundError;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.dto.GuildDTO;
import javax.annotation.Nonnull;

public interface GuildRepository extends Repository{
  @Nonnull
  GuildDTO fetch(@Nonnull GuildId query) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError;
  @Nonnull
  GuildDTO fetch(@Nonnull String id) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError;
  @Nonnull
  GuildDTO create(@Nonnull GuildCreationQuery query);
  void delete(@Nonnull GuildId guild);
}
