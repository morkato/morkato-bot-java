package org.morkato.bmt.api.entity.guild;

import org.morkato.bmt.api.dto.ArtDTO;
import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.art.Art;
import org.morkato.bmt.api.entity.impl.ObjectResolverImpl;
import org.morkato.bmt.api.repository.RepositoryCentral;

import javax.annotation.Nonnull;
import java.util.Objects;

public class GuildArtResolver
  extends ObjectResolverImpl<Art>
  implements ObjectResolver<Art> {
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
      this.add(Art.create(repository, guild, dto));
  }
}
