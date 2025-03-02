package org.morkato.api.repository;

import org.morkato.api.exception.guild.GuildIdInvalidError;
import org.morkato.api.exception.guild.GuildNotFoundError;
import org.morkato.api.exception.repository.RepositoryInternalError;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.queries.GuildCreateQuery;
import org.morkato.api.repository.queries.GuildIdQuery;

import javax.annotation.Nonnull;
import java.util.concurrent.Future;

public interface GuildRepository extends Repository {
  @Nonnull
  Guild fetch(@Nonnull GuildIdQuery query) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError;
  @Nonnull
  Guild fetch(@Nonnull String id) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError;
  @Nonnull
  Guild create(@Nonnull GuildCreateQuery query);
  @Nonnull
  void delete(@Nonnull Guild guild);
}
