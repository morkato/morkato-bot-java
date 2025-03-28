package org.morkato.api.repository;

import org.morkato.api.dto.GuildDTO;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.entity.impl.guild.ApiGuildImpl;
import org.morkato.api.exception.repository.RepositoryNotImplementedException;
import org.morkato.api.repository.ability.AbilityRepository;
import org.morkato.api.repository.family.FamilyRepository;
import org.morkato.api.repository.trainer.TrainerRepository;
import org.morkato.api.repository.attack.AttackRepository;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.api.repository.art.ArtRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.morkato.api.repository.user.UserRepository;

import javax.annotation.Nonnull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SimpleRepositoryCentral implements RepositoryCentral {
  private AbilityRepository abilityRepository;
  private FamilyRepository familyRepository;
  private AttackRepository attackRepository;
  private TrainerRepository trainerRepository;
  private GuildRepository guildRepository;
  private UserRepository userRepository;
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

  @Nonnull
  @Override
  public AbilityRepository ability() {
    if (Objects.isNull(abilityRepository))
      throw new RepositoryNotImplementedException(AbilityRepository.class);
    return abilityRepository;
  }

  @Nonnull
  @Override
  public FamilyRepository family() {
    if (Objects.isNull(familyRepository))
      throw new RepositoryNotImplementedException(FamilyRepository.class);
    return familyRepository;
  }

  @Nonnull
  @Override
  public UserRepository user() {
    if (Objects.isNull(userRepository))
      throw new RepositoryNotImplementedException(UserRepository.class);
    return userRepository;
  }
}
