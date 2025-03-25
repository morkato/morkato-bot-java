package org.morkato.api.repository.attack;

import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.entity.attack.AttackId;
import org.morkato.api.repository.Repository;
import org.morkato.api.dto.AttackDTO;

public interface AttackRepository extends Repository{
  AttackDTO[] fetchAll(AttackFetchAllQuery query) throws RepositoryException;
  AttackDTO fetch(AttackId query) throws RepositoryException;
  AttackDTO create(AttackCreationQuery query) throws RepositoryException;
  AttackDTO update(AttackUpdateQuery query) throws RepositoryException;
  void delete(AttackId query) throws RepositoryException;
}
