package org.morkato.api.repository;

import javax.annotation.Nonnull;

public class SimpleRepositoryCentral implements RepositoryCentral {
  private final GuildRepository guildRepository;
  private final ArtRepository artRepository;
  public static SimpleRepositoryCentral create(
    @Nonnull GuildRepository guildRepository,
    @Nonnull ArtRepository artRepository
  ) {
    return new SimpleRepositoryCentral(guildRepository, artRepository);
  }
  protected SimpleRepositoryCentral(
    @Nonnull GuildRepository guildRepository,
    @Nonnull ArtRepository artRepository
  ) {
    this.guildRepository = guildRepository;
    this.artRepository = artRepository;
  }

  @Override
  @Nonnull
  public GuildRepository guild() {
    return guildRepository;
  }

  @Nonnull
  @Override
  public ArtRepository art() {
    return this.artRepository;
  }
}
