package org.morkato.bmt.startup.management;

import org.morkato.bmt.generated.ParsersStaticRegistries;
import org.morkato.bmt.generated.registries.ObjectParserRegistry;
import org.morkato.bmt.generated.registries.SlashMapperRegistry;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.SlashMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Objects;
import java.util.Collection;
import java.util.Map;

public class StartupParsersManagement
  extends StartupManagement
  implements RegistrationFactory<ParsersStaticRegistries> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartupParsersManagement.class);
  private final Map<Class<?>, ObjectParser<?>> objectparsers = new HashMap<>();
  private final Map<Class<?>, SlashMapper<?>> slashmappers = new HashMap<>();

  public StartupParsersManagement(DependenceInjection injector) {
    super(injector);
  }

  public void registerObjectParser(Class<?> clazz, ObjectParser<?> parser) {
    try {
      if (Objects.nonNull(objectparsers.get(clazz))) {
        LOGGER.warn("Ignoring register for object parser: {}. This parser is already registered.", clazz.getName());
        return;
      }
      this.writeInRegistry(parser);
      objectparsers.put(clazz, parser);
      LOGGER.debug("Object Parser: {} has been registered for bootstrap.", parser);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then registry object parser: {}.", parser, exc);
    }
  }

  public void registerSlashMapper(Class<?> clazz, SlashMapper<?> slashmapper) {
    try {
      if (Objects.nonNull(slashmappers.get(clazz))) {
        LOGGER.warn("Ignoring register for slash mapper: {}. This mapper is already registered.", clazz.getName());
        return;
      }
      this.writeInRegistry(slashmapper);
      slashmappers.put(clazz, slashmapper);
      LOGGER.debug("Slash Mapper: {} has been registered for bootstrap.", slashmapper);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred on then registry slash mapper: {}.", slashmapper, exc);
    }
  }

  private void flushObjectParser(
    Class<?> clazz,
    ObjectParser<?> parser,
    Map<Class<?>, ObjectParserRegistry<?>> parsers,
    ReferenceGetter references
  ) {
    try {
      references.setCurrentParser(clazz);
      parser.flush(references);
      parsers.put(clazz, new ObjectParserRegistry<>(parser, clazz));
      LOGGER.info("Success to flush object parser: {} ({}). The content is available for requests.", parser, clazz);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred while flushing object parser: {}. Cleaning all parsers referred by this one.", parser.getClass().getName(), exc);
      Collection<Class<?>> referencedBy = references.getReferred(clazz);
      for (Class<?> referred : referencedBy) {
        parsers.remove(referred);
        LOGGER.info("Removing parser for: {} as a referred parser invoked an exception.", referred.getName());
      }
    }
  }

  private Map<Class<?>, ObjectParserRegistry<?>> flushObjectParsers() {
    try {
      final Map<Class<?>, ObjectParserRegistry<?>> parsers = new HashMap<>();
      final ReferenceGetter references = new ReferenceGetter(objectparsers);
      for (Map.Entry<Class<?>, ObjectParser<?>> entry : objectparsers.entrySet()) {
        Class<?> clazz = entry.getKey();
        ObjectParser<?> parser = entry.getValue();
        this.flushObjectParser(clazz, parser, parsers, references);
      }
      return parsers;
    } catch (Exception exc) {
      LOGGER.error("An critical error occurred while flush all object parsers. Ignoring all.", exc);
      return Map.of();
    }
  }

  private Map<Class<?>, SlashMapperRegistry<?>> flushSlashMappers() {
    final Map<Class<?>, SlashMapperRegistry<?>> mappers = new HashMap<>();
    for (Map.Entry<Class<?>, SlashMapper<?>> entry : slashmappers.entrySet()) {
      Class<?> clazz = entry.getKey();
      SlashMapper<?> slashmapper = entry.getValue();
      mappers.put(clazz, new SlashMapperRegistry<>(slashmapper));
      LOGGER.info("Success to flush slash mapper: {} ({}). The content is available for requests.", slashmapper, clazz);
    }
    return mappers;
  }

  @Override
  public ParsersStaticRegistries flush() {
    LOGGER.info("Flushing all object parsers and slash mappers to generate static content.");
    final Map<Class<?>, ObjectParserRegistry<?>> objectparsers = this.flushObjectParsers();
    final Map<Class<?>, SlashMapperRegistry<?>> slashmappers = this.flushSlashMappers();
    LOGGER.info("All object parsers and slash mappers has already flushed!");
    return new ParsersStaticRegistries(objectparsers, slashmappers);
  }
}
