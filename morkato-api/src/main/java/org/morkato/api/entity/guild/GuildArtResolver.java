package org.morkato.api.entity.guild;

import org.morkato.api.dto.ArtDTO;
import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.impl.ObjectResolverImpl;
import org.morkato.api.entity.impl.art.ApiArtImpl;
import org.morkato.api.repository.RepositoryCentral;

import javax.annotation.Nonnull;
import java.util.Objects;

public class GuildArtResolver extends ObjectResolverImpl<Art> implements ObjectResolver<Art> {
  private final RepositoryCentral repository;
  private final Guild guild;
  public GuildArtResolver(@Nonnull Guild guild, @Nonnull RepositoryCentral repository) {
    Objects.requireNonNull(guild);
    Objects.requireNonNull(repository);
    this.repository = repository;
    this.guild = guild;
  }
  @Override
  protected void resolveImpl() {
    ArtDTO[] arts = repository.art().fetchAll(guild.getId());
    for (ArtDTO dto : arts)
      this.add(new ApiArtImpl(repository, guild, dto));
  }

  @Override
  public Art get(String id) {
    return super.get(id);
  }
}
