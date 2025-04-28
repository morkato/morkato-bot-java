package org.morkato.api.exception.guild;

import lombok.Getter;
import org.morkato.api.exception.NotFoundError;

@Getter
public class GuildNotFoundException extends NotFoundError{
  private final String id;
  public GuildNotFoundException(String id) {
    super("Guild with id: " + id + " is not found.");
    this.id = id;
  }
}
