package org.morkato.api.entity.impl.art;

import org.morkato.api.dto.ArtDTO;
import org.morkato.api.entity.ApiObjectResolver;
import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.art.ArtType;
import org.morkato.api.entity.art.ArtUpdateBuilder;
import org.morkato.api.entity.art.AttackArtResolver;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.ArtRepository;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.repository.queries.attack.AttackUpdateQuery;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ApiArtImpl implements Art {
  private final RepositoryCentral central;
  private final AttackArtResolver attacks;
  private String guildId;
  private Guild guild;
  private String id;
  private String name;
  private ArtType type;
  private String description;
  private String banner;

  public ApiArtImpl(
    @Nonnull RepositoryCentral central,
    @Nonnull Guild guild,
    @Nonnull ArtDTO dto
  ) {
    Objects.requireNonNull(central);
    Objects.requireNonNull(guild);
    Objects.requireNonNull(dto);
    this.central = central;
    this.guild = guild;
    if (!guild.getId().equals(dto.getGuildId()))
      /* TODO: Add custom error for stupid context. */
      throw new RuntimeException("");
    this.fromDTO(dto);
    /* TODO: Add logic for attacks in art context >_< */
    this.attacks = new AttackArtResolver(this);
  }

  @Override
  public String toString() {
    return Art.representation(this);
  }

  private void fromDTO(ArtDTO dto) {
    this.guildId = dto.getGuildId();
    this.id = dto.getId();
    this.name = dto.getName();
    this.type = dto.getType();
    this.description = dto.getDescription();
    this.banner = dto.getBanner();
  }

  @Nonnull
  @Override
  public String getGuildId() {
    Objects.requireNonNull(this.guildId);
    return this.guildId;
  }

  @Nonnull
  @Override
  public Guild getGuild() {
    Objects.requireNonNull(this.guild);
    return this.guild;
  }

  @Nonnull
  @Override
  public ObjectResolver<Attack> getAttackResolver() {
    return this.attacks;
  }

  @Nonnull
  @Override
  public String getId() {
    Objects.requireNonNull(this.id);
    return this.id;
  }

  @Nonnull
  @Override
  public String getName() {
    Objects.requireNonNull(this.name);
    return this.name;
  }

  @Nonnull
  @Override
  public ArtType getType() {
    Objects.requireNonNull(this.type);
    return this.type;
  }

  @Nullable
  @Override
  public String getDescription() {
    return this.description;
  }

  @Nullable
  @Override
  public String getBanner() {
    return this.banner;
  }

  @Override
  public ArtUpdateBuilder doUpdate() {
    return new ApiArtUpdateBuilderImpl(this);
  }

  @Override
  public void update(AttackUpdateQuery query){
    throw new RuntimeException("Not implemented error.");
  }

  @Override
  public Art delete() {
    ArtRepository arts = central.art();
    arts.delete(this);
    return this;
  }
}
