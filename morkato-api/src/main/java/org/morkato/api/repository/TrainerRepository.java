package org.morkato.api.repository;

import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.queries.TrainerCreationQuery;
import org.morkato.api.repository.queries.TrainerFetchAllQuery;
import org.morkato.api.repository.queries.TrainerUpdateQuery;
import org.morkato.api.entity.trainer.TrainerId;
import org.morkato.api.dto.TrainerDTO;

public interface TrainerRepository extends Repository {
  static void createAll(TrainerRepository repository, Iterable<TrainerCreationQuery> queries) {
    for (TrainerCreationQuery trainer : queries)
      repository.create(trainer);
  }
  TrainerDTO[] fetchAll(TrainerFetchAllQuery query) throws RepositoryException;
  TrainerDTO fetch(TrainerId query) throws RepositoryException;
  TrainerDTO create(TrainerCreationQuery query) throws RepositoryException;
  TrainerDTO update(TrainerUpdateQuery query) throws RepositoryException;
  void delete(TrainerId query) throws RepositoryException;
}
