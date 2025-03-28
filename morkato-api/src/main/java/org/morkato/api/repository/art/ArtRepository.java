package org.morkato.api.repository.art;

import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.Repository;
import org.morkato.api.entity.art.ArtId;
import org.morkato.api.dto.ArtDTO;
import javax.annotation.Nonnull;

public interface ArtRepository extends Repository{
  @Nonnull
  ArtDTO[] fetchAll(String guildId) throws RepositoryException;
  @Nonnull
  ArtDTO fetch(ArtId query) throws RepositoryException;
  @Nonnull
  ArtDTO create(ArtCreationQuery query) throws RepositoryException;
  @Nonnull
  void update(ArtUpdateQuery query) throws RepositoryException;
  void delete(ArtId art) throws RepositoryException;
}
