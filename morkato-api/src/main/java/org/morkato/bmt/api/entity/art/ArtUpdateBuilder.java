package org.morkato.bmt.api.entity.art;

import org.morkato.bmt.api.DataBuilder;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ArtUpdateBuilder extends DataBuilder<Art> {
  ArtUpdateBuilder name(@Nonnull String name);
  ArtUpdateBuilder type(@Nonnull ArtType type);
  ArtUpdateBuilder description(@Nullable String description);
  ArtUpdateBuilder banner(@Nullable String banner);
}
