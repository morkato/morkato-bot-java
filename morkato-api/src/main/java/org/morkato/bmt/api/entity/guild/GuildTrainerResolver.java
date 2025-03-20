package org.morkato.bmt.api.entity.guild;

import org.morkato.bmt.api.dto.TrainerDTO;
import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.impl.ObjectResolverImpl;
import org.morkato.bmt.api.entity.impl.trainer.TrainerImpl;
import org.morkato.bmt.api.entity.trainer.Trainer;
import org.morkato.bmt.api.repository.RepositoryCentral;
import org.morkato.bmt.api.repository.queries.TrainerFetchAllQuery;

import javax.annotation.Nonnull;
import java.util.Objects;

public class GuildTrainerResolver extends ObjectResolverImpl<Trainer> implements ObjectResolver<Trainer> {
  private final RepositoryCentral repository;
  private final Guild guild;
  public GuildTrainerResolver(@Nonnull Guild guild, @Nonnull RepositoryCentral repository) {
    Objects.requireNonNull(guild);
    Objects.requireNonNull(repository);
    this.repository = repository;
    this.guild = guild;
  }
  @Override
  protected void resolveImpl() {
    TrainerDTO[] dtos = repository.trainer().fetchAll(new TrainerFetchAllQuery(guild.getId()));
    for (TrainerDTO dto : dtos)
      this.add(new TrainerImpl(guild, dto));
  }
}
