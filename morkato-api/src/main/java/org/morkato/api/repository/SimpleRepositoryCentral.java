package org.morkato.api.repository;

import javax.annotation.Nonnull;

public class SimpleRepositoryCentral implements RepositoryCentral {
  private final GuildRepository guildRepository;
  public static SimpleRepositoryCentral create(
    @Nonnull GuildRepository guildRepository
  ) {
    return new SimpleRepositoryCentral(guildRepository);
  }
  protected SimpleRepositoryCentral(
    @Nonnull GuildRepository guildRepository
  ) {
    this.guildRepository = guildRepository;
  }

  @Override
  @Nonnull
  public GuildRepository guild() {
    return guildRepository;
  }
}
