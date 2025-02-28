package org.morkato.api.repository;

import org.morkato.api.repository.queries.ArtUpdateQuery;
import org.morkato.api.repository.queries.ArtCreationQuery;
import org.morkato.api.repository.queries.ArtFetchQuery;
import org.morkato.api.entity.art.Art;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;

public interface ArtRepository {
  @Nonnull
  Art[] fetchAll(String guildId);
  @Nonnull
  Art fetch(ArtFetchQuery query);
  @Nonnull
  Art create(ArtCreationQuery query);
  @Nonnull
  Art update(ArtUpdateQuery query);
  @Nonnull
  Art delete(Art art);
}
