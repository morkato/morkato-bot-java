package org.morkato.api.repository;

import org.morkato.api.repository.queries.ArtUpdateQuery;
import org.morkato.api.repository.queries.ArtCreationQuery;
import org.morkato.api.repository.queries.ArtFetchQuery;
import org.morkato.api.entity.art.Art;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;

public interface ArtRepository {
  @Nonnull
  Future<Art[]> fetchAll(String guildId);
  @Nonnull
  Future<Art> fetch(ArtFetchQuery query);
  @Nonnull
  Future<Art> create(ArtCreationQuery query);
  @Nonnull
  Future<Art> update(ArtUpdateQuery query);
  @Nonnull
  Future<Art> delete(ArtFetchQuery query);
}
