package org.morkato.api.entity.art;

import org.morkato.api.entity.EntityNamed;
import org.morkato.api.dto.ArtDTO;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public interface ArtPayload extends EntityNamed, ArtId {
  @Nonnull
  ArtType getType();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
}
