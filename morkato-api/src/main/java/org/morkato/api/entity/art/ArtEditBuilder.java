package org.morkato.api.entity.art;

import org.morkato.api.DataBuilder;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ArtEditBuilder extends DataBuilder<Art> {
  ArtEditBuilder name(@Nonnull String name);
  ArtEditBuilder type(@Nonnull ArtType type);
  ArtEditBuilder description(@Nullable String description);
  ArtEditBuilder banner(@Nullable String banner);
}
