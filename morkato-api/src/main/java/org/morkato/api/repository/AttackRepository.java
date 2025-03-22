package org.morkato.api.repository;

import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.attack.AttackId;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.queries.attack.AttackCreationQuery;
import org.morkato.api.repository.queries.attack.AttackFetchAllQuery;
import org.morkato.api.repository.queries.attack.AttackUpdateQuery;

public interface AttackRepository extends Repository {
  Attack[] fetchAll(AttackFetchAllQuery query) throws RepositoryException;
  Attack fetch(AttackId query) throws RepositoryException;
  Attack create(AttackCreationQuery query) throws RepositoryException;
  Attack update(AttackUpdateQuery query) throws RepositoryException;
  void delete(AttackId query) throws RepositoryException;
}
