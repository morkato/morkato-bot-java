package org.morkato.api.entity.art;

import org.morkato.api.entity.*;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.dto.ArtDTO;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public interface Art
  extends DeleteApiModel<Art>,
          UpdateApiModel<Art, ArtUpdateBuilder>,
          ArtPayload,
          ArtId {
  static String representation(Art art) {
    return "Art[guild = " + art.getGuild() + ", id = " + art.getId() + ", name = " + art.getName() + "]";
  }
  @Nonnull
  Guild getGuild();
  @Nonnull
  ObjectResolver<Attack> getAttackResolver();
}
