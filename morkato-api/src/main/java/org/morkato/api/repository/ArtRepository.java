package org.morkato.api.repository;

import org.morkato.api.dto.ArtDTO;
import org.morkato.api.entity.art.ArtId;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.repository.queries.ArtUpdateQuery;
import org.morkato.api.repository.queries.ArtCreationQuery;
import org.morkato.api.entity.art.Art;

import javax.annotation.Nonnull;

public interface ArtRepository extends Repository {
  @Nonnull
  ArtDTO[] fetchAll(String guildId) throws RepositoryException;
  @Nonnull
  ArtDTO fetch(ArtId query) throws RepositoryException;
  @Nonnull
  ArtDTO create(ArtCreationQuery query) throws RepositoryException;
  @Nonnull
  ArtDTO update(ArtUpdateQuery query) throws RepositoryException;
  @Nonnull
  void delete(ArtId art) throws RepositoryException;
}
