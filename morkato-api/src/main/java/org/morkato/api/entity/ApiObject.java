package org.morkato.api.entity;

import javax.annotation.Nonnull;

public interface ApiObject extends ObjectId {
  @Nonnull
  String getName();
}
