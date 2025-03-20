package org.morkato.bmt.api.memory;

import jakarta.validation.Validator;
import org.morkato.bmt.api.SnowflakeIdGenerator;
import org.morkato.bmt.api.dto.TrainerDTO;
import org.morkato.bmt.api.entity.trainer.TrainerId;
import org.morkato.bmt.api.exception.repository.RepositoryException;
import org.morkato.bmt.api.repository.TrainerRepository;
import org.morkato.bmt.api.repository.queries.TrainerCreationQuery;
import org.morkato.bmt.api.repository.queries.TrainerFetchAllQuery;
import org.morkato.bmt.api.repository.queries.TrainerUpdateQuery;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class MemoryTrainerRepository implements TrainerRepository {
  private final Map<String, Map<String, TrainerDTO>> trainers = new HashMap<>();
  private final Validator validator;
  public MemoryTrainerRepository(Validator validator) {
    Objects.requireNonNull(validator);
    this.validator = validator;
  }
  @Override
  public TrainerDTO[] fetchAll(TrainerFetchAllQuery query) throws RepositoryException{
    Map<String, TrainerDTO> trainers = this.trainers.get(query.getGuildId());
    if (Objects.isNull(trainers))
      return new TrainerDTO[0];
    return trainers.values().toArray(TrainerDTO[]::new);
  }

  @Override
  public TrainerDTO fetch(TrainerId query) throws RepositoryException {
    Map<String, TrainerDTO> trainers = this.trainers.get(query.getGuildId());
    if (Objects.isNull(trainers))
      throw new RepositoryException("Trainer not found.");
    TrainerDTO dto = trainers.get(query.getId());
    if (Objects.isNull(dto))
      throw new RepositoryException("Trainer not found.");
    return dto;
  }

  @Override
  public TrainerDTO create(TrainerCreationQuery query) throws RepositoryException {
    TrainerDTO dto = TrainerDTO.from(query)
      .setId(SnowflakeIdGenerator.staticNextAsString());
    dto.validateForCreate(validator);
    Map<String, TrainerDTO> trainers = this.trainers.computeIfAbsent(query.guildId(), key -> new HashMap<>());
    trainers.put(dto.getId(), dto);
    return dto;
  }

  @Override
  public TrainerDTO update(TrainerUpdateQuery query) throws RepositoryException {
    throw new RepositoryException("TrainerRepository::update is not implemented");
  }

  @Override
  public void delete(TrainerId query) throws RepositoryException{
    throw new RepositoryException("TrainerRepository::delete is not implemented");
  }
}
