package org.morkato.api.entity.impl.art;

import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.art.ArtType;
import org.morkato.api.entity.art.ArtUpdateBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ApiArtUpdateBuilderImpl implements ArtUpdateBuilder {
  public ApiArtUpdateBuilderImpl(Art art){
  }

  @Override
  public ArtUpdateBuilder name(@Nonnull String name) {
    return this;
  }

  @Override
  public ArtUpdateBuilder type(@Nonnull ArtType type) {
    return this;
  }

  @Override
  public ArtUpdateBuilder description(@Nullable String description) {
    return this;
  }

  @Override
  public ArtUpdateBuilder banner(@Nullable String banner) {
    return this;
  }

  @Override
  public Art execute() {
    return null;
  }
}
