package org.morkato.api.repository.art;

import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.entity.art.ArtPayload;
import org.morkato.api.repository.Repository;
import org.morkato.api.entity.art.ArtId;
import javax.annotation.Nonnull;

public interface ArtRepository extends Repository {
  @Nonnull
  ArtPayload[] fetchAll(String guildId) throws RepositoryException;
  @Nonnull
  ArtPayload fetch(ArtId query) throws RepositoryException;
  @Nonnull
  ArtPayload create(ArtCreationQuery query) throws RepositoryException;
  void update(ArtUpdateQuery query) throws RepositoryException;
  void delete(ArtId art) throws RepositoryException;
}
