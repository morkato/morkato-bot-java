package org.morkato.database.repository.rpg;

import org.morkato.api.entity.MorkatoModelType;
import org.morkato.api.entity.ObjectEntity;
import org.morkato.api.entity.rpg.RpgId;
import org.morkato.api.entity.rpg.RpgPayload;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.exception.repository.RepositoryInternalError;
import org.morkato.api.exception.rpg.RpgNotFoundException;
import org.morkato.api.repository.rpg.RpgCreationQuery;
import org.morkato.database.PsqlRepositoryStatement;
import org.morkato.database.mapper.ObjectEntityMapper;
import org.morkato.utility.mcisid.McisidUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.morkato.database.mapper.RpgTupleRowMapper;
import org.morkato.api.repository.rpg.RpgRepository;
import org.morkato.jdbc.QueryExecutor;
import org.morkato.jdbc.QueryContent;
import org.morkato.api.dto.RpgDTO;
import org.springframework.jdbc.core.RowMapper;

public class PsqlRpgRepository
  implements RpgRepository {
  private final RowMapper<RpgDTO> mapper = new RpgTupleRowMapper();
  private final RowMapper<ObjectEntity> objmapper = new ObjectEntityMapper();
  private final PsqlRepositoryStatement statement;

  @QueryContent("rpg/rpg-find-by-id.sql")
  protected QueryExecutor findByIdQuery;
  @QueryContent("rpg/rpg-creation.sql")
  protected  QueryExecutor creationQuery;
  @QueryContent("rpg/rpg-delete-by-id.sql")
  protected QueryExecutor deleteByIdQuery;

  public static void assertPreRpgIdExceptions(RpgId query) throws RpgNotFoundException {
    if (!McisidUtil.isIdModel(query.getId(), MorkatoModelType.RPG))
      throw new RpgNotFoundException(query);
  }
  public PsqlRpgRepository(PsqlRepositoryStatement statement) {
    this.statement = statement;
  }


  @Override
  public RpgPayload find(RpgId query) throws RpgNotFoundException {
    assertPreRpgIdExceptions(query);
    try {
      return findByIdQuery.queryForObject(statement.getJdbc(), mapper, query.getId());
    } catch (EmptyResultDataAccessException exc) {
      throw new RpgNotFoundException(query);
    }
  }

  @Override
  public RpgPayload create(RpgCreationQuery query) throws RepositoryInternalError {
    statement.assertTransactionActive();
    final RpgDTO normalized = RpgDTO.normalizeDefault(query);
    final ObjectEntity entity = creationQuery.queryForObject(
      statement.getJdbc(), objmapper, normalized.getHumanInitialLife(),
      normalized.getOniInitialLife(), normalized.getHybridInitialLife(), normalized.getBreathInitial(),
      normalized.getBloodInitial(), normalized.getAbilityRoll(), normalized.getFamilyRoll());
    return normalized.setId(entity.getId());
  }

  @Override
  public RpgPayload create() throws RepositoryException{
    return this.create(new RpgCreationQuery());
  }

  @Override
  public void delete(RpgId query) throws RepositoryException {
    statement.assertTransactionActive();
    assertPreRpgIdExceptions(query);
    int count = deleteByIdQuery.update(statement.getJdbc(), query.getId());
    if (count == 0)
      throw new RpgNotFoundException(query);
    else if (count != 1)
      throw new RepositoryException("Internal error");
  }
}
