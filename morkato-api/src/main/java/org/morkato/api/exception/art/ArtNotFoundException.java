package org.morkato.api.exception.art;

import org.morkato.api.entity.art.ArtId;
import org.morkato.api.exception.repository.RepositoryException;

public class ArtNotFoundException extends RepositoryException {
  private final ArtId artId;

  public ArtNotFoundException(ArtId query) {
    super("Art with id: " + query);
    this.artId = query;
  }
}
