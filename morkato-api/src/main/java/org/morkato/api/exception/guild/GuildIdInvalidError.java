package org.morkato.api.exception.guild;

import lombok.Getter;
import org.morkato.api.exception.repository.RepositoryError;

@Getter
public class GuildIdInvalidError extends RepositoryError{
  private final String id;
  public GuildIdInvalidError(String id) {
    super("Guild ID: " + id + " is invalid");
    this.id = id;
  }
}
