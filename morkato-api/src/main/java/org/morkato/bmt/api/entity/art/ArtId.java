package org.morkato.bmt.api.entity.art;

import org.morkato.bmt.api.entity.ObjectId;

import javax.annotation.Nonnull;

public interface ArtId extends ObjectId {
  @Nonnull
  String getGuildId();
}
