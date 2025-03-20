package org.morkato.bmt.api.repository;

import org.morkato.bmt.api.dto.ArtDTO;
import org.morkato.bmt.api.entity.art.ArtId;
import org.morkato.bmt.api.exception.repository.RepositoryException;
import org.morkato.bmt.api.repository.queries.ArtUpdateQuery;
import org.morkato.bmt.api.repository.queries.ArtCreationQuery;

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
  void delete(ArtId art) throws RepositoryException;
}
