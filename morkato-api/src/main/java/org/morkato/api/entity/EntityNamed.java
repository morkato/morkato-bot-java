package org.morkato.api.entity;

import javax.annotation.Nonnull;

public interface EntityNamed extends ObjectId {
  @Nonnull
  String getName();
}
