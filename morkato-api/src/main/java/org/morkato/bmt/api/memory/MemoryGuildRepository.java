package org.morkato.bmt.api.memory;

import jakarta.validation.Validator;
import org.morkato.bmt.api.dto.GuildDTO;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.entity.impl.guild.ApiGuildImpl;
import org.morkato.bmt.api.entity.values.GuildDefaultValue;
import org.morkato.bmt.api.exception.guild.GuildIdInvalidError;
import org.morkato.bmt.api.exception.guild.GuildNotFoundError;
import org.morkato.bmt.api.exception.repository.RepositoryException;
import org.morkato.bmt.api.exception.repository.RepositoryInternalError;
import org.morkato.bmt.api.repository.GuildRepository;
import org.morkato.bmt.api.repository.RepositoryCentral;
import org.morkato.bmt.api.repository.queries.GuildCreateQuery;
import org.morkato.bmt.api.repository.queries.GuildIdQuery;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryGuildRepository implements GuildRepository {
  private final Map<String, Guild> guilds = new HashMap<>();
  private final RepositoryCentral central;
  private final Validator validator;

  public MemoryGuildRepository(RepositoryCentral central, Validator validator) {
    this.central = central;
    this.validator = validator;
  }

  @Override
  @Nonnull
  public Guild fetch(@Nonnull GuildIdQuery query) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError {
    Guild guild = guilds.get(query.id());
    if (Objects.isNull(guild))
      return this.create(new GuildCreateQuery().setId(query.id()));
    return guild;
  }

  @Override
  @Nonnull
  public Guild fetch(@Nonnull String id) throws RepositoryInternalError, GuildNotFoundError, GuildIdInvalidError{
    return this.fetch(new GuildIdQuery(id));
  }

  @Override
  @Nonnull
  public Guild create(@Nonnull GuildCreateQuery query) {
    GuildDTO dto =GuildDefaultValue.from(query).getDTO()
      .setId(query.getId());
    dto.validate(validator);
    ApiGuildImpl guild = new ApiGuildImpl(central, dto);
    this.guilds.put(guild.getId(), guild);
    return guild;
  }

  @Override
  @Nonnull
  public void delete(@Nonnull Guild guild){
    throw new RepositoryException("GuildRepository::delete is not implemented");
  }
}
