package org.morkato.api.repository;

import org.morkato.api.dto.GuildDTO;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.entity.impl.guild.ApiGuildImpl;
import org.morkato.api.exception.repository.RepositoryNotImplementedException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.annotation.Nonnull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SimpleRepositoryCentral implements RepositoryCentral {
  private AttackRepository attackRepository;
  private TrainerRepository trainerRepository;
  private GuildRepository guildRepository;
  private ArtRepository artRepository;

  @Override
  @Nonnull
  public GuildRepository guild() {
    if (Objects.isNull(guildRepository))
      throw new RepositoryNotImplementedException(GuildRepository.class);
    return guildRepository;
  }

  @Override
  @Nonnull
  public TrainerRepository trainer() {
    if (Objects.isNull(trainerRepository))
      throw new RepositoryNotImplementedException(TrainerRepository.class);
    return trainerRepository;
  }

  @Nonnull
  @Override
  public AttackRepository attack() {
    if (Objects.isNull(attackRepository))
      throw new RepositoryNotImplementedException(AttackRepository.class);
    return attackRepository;
  }

  @Nonnull
  @Override
  public ArtRepository art() {
    if (Objects.isNull(artRepository))
      throw new RepositoryNotImplementedException(ArtRepository.class);
    return artRepository;
  }

  @Override
  @Nonnull
  public Guild fetchGuild(GuildId id) {
    final GuildDTO dto = guild().fetch(id);
    return new ApiGuildImpl(this, dto);
  }

  @Override
  public Guild getCachedGuild(GuildId id) {
    throw new RuntimeException("SimpleRepositorySimple::getCachedGuild is not yet implemented.");
  }
}
