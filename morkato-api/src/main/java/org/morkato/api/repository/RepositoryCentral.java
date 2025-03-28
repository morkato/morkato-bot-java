package org.morkato.api.repository;

import org.morkato.api.repository.ability.AbilityRepository;
import org.morkato.api.repository.family.FamilyRepository;
import org.morkato.api.repository.trainer.TrainerRepository;
import org.morkato.api.repository.attack.AttackRepository;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.api.repository.art.ArtRepository;
import org.morkato.api.repository.user.UserRepository;

import javax.annotation.Nonnull;

public interface RepositoryCentral {
  @Nonnull
  GuildRepository guild();
  @Nonnull
  TrainerRepository trainer();
  @Nonnull
  AttackRepository attack();
  @Nonnull
  ArtRepository art();
  @Nonnull
  AbilityRepository ability();
  @Nonnull
  FamilyRepository family();
  @Nonnull
  UserRepository user();
}
