package org.morkato.api.exception.guild;

import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.exception.repository.RepositoryException;

public class GuildAlreadyExistsError extends RepositoryException {
  private final GuildId guild;

  public GuildAlreadyExistsError(GuildId guild) {
    super("Guild with ID: " + guild.getId() + " already exists.");
    this.guild = guild;
  }

  public GuildId getGuild() {
    return guild;
  }
}
