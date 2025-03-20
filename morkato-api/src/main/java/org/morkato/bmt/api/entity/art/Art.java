package org.morkato.bmt.api.entity.art;

import org.morkato.bmt.api.repository.RepositoryCentral;
import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.attack.Attack;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.entity.ApiObject;
import org.morkato.bmt.api.DeleteApiModel;
import org.morkato.bmt.api.UpdateApiModel;
import org.morkato.bmt.api.entity.impl.art.ApiArtImpl;
import org.morkato.bmt.api.dto.ArtDTO;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    return new ApiArtImpl(central, guild, dto);
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
