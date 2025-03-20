package org.morkato.bmt.api.repository;

import jakarta.validation.Validator;
import org.morkato.bmt.api.exception.repository.RepositoryException;
import org.morkato.bmt.api.repository.queries.TrainerCreationQuery;
import org.morkato.bmt.api.repository.queries.TrainerFetchAllQuery;
import org.morkato.bmt.api.repository.queries.TrainerUpdateQuery;
import org.morkato.bmt.api.memory.MemoryTrainerRepository;
import org.morkato.bmt.api.entity.trainer.TrainerId;
import org.morkato.bmt.api.dto.TrainerDTO;

public interface TrainerRepository extends Repository {
  static TrainerRepository createInMemory(Validator validator) {
    return new MemoryTrainerRepository(validator);
  }

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
