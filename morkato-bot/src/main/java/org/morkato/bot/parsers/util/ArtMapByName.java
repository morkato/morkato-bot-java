package org.morkato.bot.parsers.util;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.art.Art;

public class ArtMapByName extends ObjectMapByName<Art> {
  @Override
  public ObjectResolver<Art> getResolver(Guild guild){
    return guild.getArtResolver();
  }
}
