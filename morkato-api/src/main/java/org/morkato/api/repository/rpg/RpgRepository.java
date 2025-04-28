package org.morkato.api.repository.rpg;

import org.morkato.api.entity.rpg.RpgPayload;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.Repository;
import org.morkato.api.entity.rpg.RpgId;

public interface RpgRepository extends Repository {
  RpgPayload find(RpgId query) throws RepositoryException;
  RpgPayload create(RpgCreationQuery query) throws RepositoryException;
  RpgPayload create() throws RepositoryException;
  void delete(RpgId query) throws RepositoryException;
}
