package org.morkato.bot.parsers.util;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.art.Art;
import org.morkato.utility.MorkatoStringUtil;

import java.util.*;
import java.util.stream.StreamSupport;

public class ArtMapByName {
  private final Map<Guild, Map<String, Art>> arts = new HashMap<>();
  public void set(Guild guild, Art art) {
    String key = MorkatoStringUtil.toMorkatoKey(art.getName());
    Map<String, Art> guildArts = arts.computeIfAbsent(guild, kg -> new HashMap<>());
    guildArts.putIfAbsent(key, art);
  }


  public Art getCached(Guild guild, String key) {
    Map<String, Art> guildArts = arts.get(guild);
    if (Objects.isNull(guildArts))
      return null;
    return guildArts.get(key);
  }

  public Art get(Guild guild, String name) {
    String key = MorkatoStringUtil.toMorkatoKey(name);
    Art art = this.getCached(guild, key);
    if (Objects.nonNull(art))
      return art;
    ObjectResolver<Art> resolver = guild.getArtResolver();
    if (!resolver.loaded())
      return null;
    Iterator<Art> unresolvedArts = StreamSupport.stream(resolver.spliterator(), false)
      .filter(it -> Objects.equals(MorkatoStringUtil.toMorkatoKey(it.getName()), key))
      .iterator();
    if (!unresolvedArts.hasNext())
      return null;
    art = unresolvedArts.next();
    if (Objects.nonNull(art))
      this.set(guild, art);
    return art;
  }
}
