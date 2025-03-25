package org.morkato.api.repository.trainer;

import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.Repository;
import org.morkato.api.entity.trainer.TrainerId;
import org.morkato.api.dto.TrainerDTO;

public interface TrainerRepository extends Repository {
  TrainerDTO[] fetchAll(TrainerFetchAllQuery query) throws RepositoryException;
  TrainerDTO fetch(TrainerId query) throws RepositoryException;
  TrainerDTO create(TrainerCreationQuery query) throws RepositoryException;
  TrainerDTO update(TrainerUpdateQuery query) throws RepositoryException;
  void delete(TrainerId query) throws RepositoryException;
}
