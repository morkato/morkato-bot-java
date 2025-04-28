package org.morkato.api.exception.rpg;

import org.morkato.api.entity.rpg.RpgId;
import org.morkato.api.exception.repository.RepositoryException;

public class RpgNotFoundException extends RepositoryException {
  public RpgNotFoundException(RpgId query) {
    super("RPG with ID: " + query.getId() + "is not found.");
  }
}
