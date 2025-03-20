package org.morkato.bmt.api.exception.guild;

import lombok.Getter;
import org.morkato.bmt.api.exception.repository.RepositoryException;

@Getter
public class GuildIdInvalidError extends RepositoryException{
  private final String id;
  public GuildIdInvalidError(String id) {
    super("Guild ID: " + id + " is invalid");
    this.id = id;
  }
}
