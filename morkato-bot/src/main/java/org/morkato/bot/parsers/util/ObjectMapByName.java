package org.morkato.bot.parsers.util;

import org.morkato.api.entity.ApiObject;
import org.morkato.api.entity.ObjectResolver;
import org.morkato.utility.MorkatoStringUtil;
import org.morkato.api.entity.guild.Guild;

import java.util.stream.StreamSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public abstract class ObjectMapByName<T extends ApiObject> {
  private final Map<Guild, Map<String, T>> objects = new HashMap<>();
  public abstract ObjectResolver<T> getResolver(Guild guild);

  public void set(Guild guild, T obj) {
    String key = MorkatoStringUtil.toMorkatoKey(obj.getName());
    Map<String, T> guildObjects = objects.computeIfAbsent(guild, kg -> new HashMap<>());
    guildObjects.putIfAbsent(key, obj);
  }

  public T getCached(Guild guild, String key) {
    Map<String, T> guildArts = objects.get(guild);
    if (Objects.isNull(guildArts))
      return null;
    return guildArts.get(key);
  }

  public T get(Guild guild,String name) {
    String key = MorkatoStringUtil.toMorkatoKey(name);
    T obj = this.getCached(guild, key);
    if (Objects.nonNull(obj))
      return obj;
    ObjectResolver<T> resolver = this.getResolver(guild);
    if (!resolver.loaded())
      return null;
    Iterator<T> unresolvedObjects = StreamSupport.stream(resolver.spliterator(), false)
      .filter(it -> Objects.equals(MorkatoStringUtil.toMorkatoKey(it.getName()), key))
      .iterator();
    if (!unresolvedObjects.hasNext())
      return null;
    obj = unresolvedObjects.next();
    if (Objects.nonNull(obj))
      this.set(guild, obj);
    return obj;
  }
}
