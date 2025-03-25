package org.morkato.api.entity.art;

import org.morkato.api.entity.ObjectId;

import javax.annotation.Nonnull;

public interface ArtId extends ObjectId {
  @Nonnull
  String getGuildId();
}
