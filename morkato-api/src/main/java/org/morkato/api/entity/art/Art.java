package org.morkato.api.entity.art;

import org.morkato.api.DeleteApiModel;
import org.morkato.api.UpdateApiModel;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.guild.Guild;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public interface Art extends ArtId, UpdateApiModel<Art,ArtUpdateBuilder>, DeleteApiModel<Art> {
  @Nonnull
  Guild getGuild();
  @Nonnull
  Map<String, Attack> getAttacks();
  @Nonnull
  String getName();
  @Nonnull
  ArtType getType();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
}
