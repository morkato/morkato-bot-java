package org.morkato.bmt.api.entity;

import javax.annotation.Nonnull;

public interface ApiObject extends ObjectId {
  @Nonnull
  String getName();
}
