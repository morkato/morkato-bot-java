package org.morkato.bmt.bot.parsers.util;

import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.entity.art.Art;

public class ArtMapByName extends ObjectMapByName<Art> {
  @Override
  public ObjectResolver<Art> getResolver(Guild guild){
    return guild.getArtResolver();
  }
}
