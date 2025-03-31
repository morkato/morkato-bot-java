package org.morkato.api.entity.art;

import org.morkato.api.entity.impl.art.ArtPayloadImpl;
import org.morkato.api.dto.ArtDTO;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public interface ArtPayload extends ArtId {
  @Nonnull
  static ArtPayload create(ArtDTO dto) {
    return new ArtPayloadImpl(dto);
  }
  @Nonnull
  String getName();
  @Nonnull
  ArtType getType();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
}
