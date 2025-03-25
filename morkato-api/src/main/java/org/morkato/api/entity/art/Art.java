package org.morkato.api.entity.art;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.impl.art.ArtEntity;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.ApiObject;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.dto.ArtDTO;
import org.morkato.api.entity.DeleteApiModel;
import org.morkato.api.entity.UpdateApiModel;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public interface Art
  extends DeleteApiModel<Art>,
          UpdateApiModel<Art, ArtUpdateBuilder>,
          ApiObject,
          ArtId {
  static Art create(
    @Nonnull RepositoryCentral central,
    @Nonnull Guild guild,
    @Nonnull ArtDTO dto
  ) {
    return new ArtEntity(central, guild, dto);
  }
  static String representation(Art art) {
    return "Art[guild = " + art.getGuild() + ", id = " + art.getId() + ", name = " + art.getName() + "]";
  }
  @Nonnull
  Guild getGuild();
  @Nonnull
  ObjectResolver<Attack> getAttackResolver();
  @Nonnull
  ArtType getType();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
}
