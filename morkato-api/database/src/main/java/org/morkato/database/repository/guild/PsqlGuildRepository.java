package org.morkato.database.repository.guild;

import org.morkato.api.dto.GuildDTO;
import org.morkato.api.entity.ObjectEntity;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.exception.guild.GuildNotFoundException;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.guild.GuildCreationQuery;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.database.PsqlRepositoryStatement;
import org.morkato.database.mapper.GuildTupleRowMapper;
import org.morkato.database.mapper.ObjectEntityMapper;
import org.morkato.jdbc.QueryContent;
import org.morkato.jdbc.QueryExecutor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class PsqlGuildRepository
  implements GuildRepository {
  private final RowMapper<GuildDTO> mapper = new GuildTupleRowMapper();
  private final RowMapper<ObjectEntity> objmapper = new ObjectEntityMapper();
  private final PsqlRepositoryStatement statement;

  @QueryContent("guild/find-guild-by-id.sql")
  private QueryExecutor findGuildByIdQuery;
  @QueryContent("guild/guild-creation.sql")
  private QueryExecutor createGuildQuery;
  @QueryContent("guild/delete-guild-by-id.sql")
  private QueryExecutor deleteGuildById;

  public PsqlGuildRepository(PsqlRepositoryStatement statement) {
    this.statement = Objects.requireNonNull(statement);
  }

  @Override
  @NonNull
  public GuildPayload fetch(GuildId query) throws RepositoryException {
    try {
      return findGuildByIdQuery.queryForObject(statement.getJdbc(), mapper, query.getId());
    } catch (EmptyResultDataAccessException exc) {
      throw new GuildNotFoundException(query.getId());
    }
  }

  @Override
  @NonNull
  public GuildPayload create(GuildCreationQuery query) throws RepositoryException {
    statement.assertTransactionActive();
    final GuildDTO normalized = GuildDTO.normalizeDefault(query);
    statement.validateForCreate(normalized);
    createGuildQuery.update(statement.getJdbc(), query.getId(), query.getRpgId(), query.getRollCategoryId(), query.getOffCategoryId());
    return normalized;
  }

  @Override
  public void delete(GuildId guild) {
    this.statement.assertTransactionActive();
    int count = deleteGuildById.update(statement.getJdbc(), guild.getId());
    if (count == 1)
      return;
    if (count == 0)
      throw new GuildNotFoundException(guild.getId());
    throw new RepositoryException("Internal Error");
  }
}
