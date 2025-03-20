package org.morkato.bmt.api.repository;

import jakarta.validation.Validator;
import org.morkato.bmt.api.exception.guild.GuildIdInvalidError;
import org.morkato.bmt.api.exception.guild.GuildNotFoundError;
import org.morkato.bmt.api.exception.repository.RepositoryInternalError;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.memory.MemoryGuildRepository;
import org.morkato.bmt.api.repository.queries.GuildCreateQuery;
import org.morkato.bmt.api.repository.queries.GuildIdQuery;

import javax.annotation.Nonnull;

public interface GuildRepository extends Repository {
  static GuildRepository createInMemory(RepositoryCentral central, Validator validator) {
    return new MemoryGuildRepository(central, validator);
  }
  @Nonnull
  Guild fetch(@Nonnull GuildIdQuery query) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError;
  @Nonnull
  Guild fetch(@Nonnull String id) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError;
  @Nonnull
  Guild create(@Nonnull GuildCreateQuery query);
  @Nonnull
  void delete(@Nonnull Guild guild);
}
