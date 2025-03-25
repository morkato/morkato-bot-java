package org.morkato.api.repository.ability;

import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.entity.ability.AbilityId;
import org.morkato.api.repository.Repository;
import org.morkato.api.dto.AbilityDTO;

public interface AbilityRepository extends Repository {
  AbilityDTO[] fetchAll(AbilityFetchQuery query) throws RepositoryException;
  AbilityDTO fetch(AbilityId query) throws RepositoryException;
  AbilityDTO create(AbilityCreationQuery query) throws RepositoryException;
  AbilityDTO update(AbilityUpdateQuery query) throws RepositoryException;
  void delete(AbilityId query) throws RepositoryException;
}
