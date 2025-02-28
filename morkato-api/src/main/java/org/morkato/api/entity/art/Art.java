package org.morkato.api.entity.art;

import org.morkato.api.DeleteApiModel;
import org.morkato.api.UpdateApiModel;
import org.morkato.api.entity.guild.Guild;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Art extends UpdateApiModel<Art, ArtEditBuilder>, DeleteApiModel<Art> {
  @Nonnull
  String getGuildId();
  @Nonnull
  Guild getGuild();
  @Nonnull
  String getId();
  @Nonnull
  String getName();
  @Nonnull
  ArtType getType();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
}
