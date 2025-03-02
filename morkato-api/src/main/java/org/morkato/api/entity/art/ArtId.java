package org.morkato.api.entity.art;

import javax.annotation.Nonnull;

public interface ArtId {
  @Nonnull
  String getGuildId();
  @Nonnull
  String getId();
}
