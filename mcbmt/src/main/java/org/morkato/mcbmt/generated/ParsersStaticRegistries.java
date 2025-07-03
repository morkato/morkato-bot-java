package org.morkato.mcbmt.generated;

import org.morkato.mcbmt.NoArgs;
import org.morkato.mcbmt.generated.registries.ObjectParserRegistry;
import org.morkato.mcbmt.generated.registries.SlashMapperRegistry;

import java.util.Iterator;
import java.util.Map;

public class ParsersStaticRegistries {
  private final ObjectParserRegistry<?>[] objectparsers;
  private final SlashMapperRegistry<?>[] slashmappers;
  private final Map<Class<?>, ObjectParserRegistry<?>> mapObjectParsers;
  private final Map<Class<?>, SlashMapperRegistry<?>> mapSlashMappers;

  public ParsersStaticRegistries(Map<Class<?>, ObjectParserRegistry<?>> objectparsers, Map<Class<?>, SlashMapperRegistry<?>> slashmappers) {
    this.mapObjectParsers = objectparsers;
    this.mapSlashMappers = slashmappers;
    this.objectparsers = new ObjectParserRegistry[objectparsers.size()];
    this.slashmappers = new SlashMapperRegistry[slashmappers.size()];
    Iterator<ObjectParserRegistry<?>> parsersIterator = objectparsers.values().iterator();
    Iterator<SlashMapperRegistry<?>> slashmappersIterator = slashmappers.values().iterator();
    for (int i = 0; i < this.objectparsers.length; ++i)
      this.objectparsers[i] = parsersIterator.next();
    for (int i = 0; i < this.slashmappers.length; ++i)
      this.slashmappers[i] = slashmappersIterator.next();
  }

  public ObjectParserRegistry<?> getObjectParser(Class<?> clazz) {
    if (NoArgs.isNoArgs(clazz))
      return NoArgs.PARSER_REGISTRY;
    return mapObjectParsers.get(clazz);
  }

  public SlashMapperRegistry<?> getSlashMapper(Class<?> clazz) {
    if (NoArgs.isNoArgs(clazz))
      return NoArgs.SLASH_MAPPER_REGISTRY;
    return mapSlashMappers.get(clazz);
  }

  public int getRegisteredObjectParserLength(){
    return objectparsers.length;
  }

  public int getRegisteredSlashMapperLength(){
    return slashmappers.length;
  }
}
