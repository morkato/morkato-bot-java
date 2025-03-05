package org.morkato.bmt.management;

import org.morkato.bmt.components.ObjectGenericParser;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.parser.RecordGenericParser;
import org.morkato.bmt.parser.IntegerParser;
import org.morkato.bmt.parser.StringParser;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArgumentManager implements RegisterManagement<ObjectParser<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentManager.class);
  private final Map<Class<?>, ObjectParser<?>> registeredParsers = new HashMap<>();
//  private final Map<Class<?>, ObjectParser<?>> specialParsers = new HashMap<>();
  private final Map<Class<?>, ObjectGenericParser<?>> genericsParsers = new HashMap<>();
  public static ArgumentManager get() {
    ArgumentManager manager = new ArgumentManager();
    manager.registerGenericParser(new RecordGenericParser());
    manager.register(new StringParser());
    manager.register(new IntegerParser());
    return manager;
  }
  public void registerGenericParser(ObjectGenericParser<?> parser) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(parser.getClass(), ObjectGenericParser.class);
    Class<?> clazz = (Class<?>)typeArguments.values().iterator().next();
    this.genericsParsers.put(clazz, parser);
  }
  public void register(ObjectParser<?> parser) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(parser.getClass(), ObjectParser.class);
    Class<?> clazz = (Class<?>)typeArguments.values().iterator().next();
    LOGGER.info("Registered ObjectParser for: {}.", clazz.getName());
    this.registeredParsers.put(clazz, parser);
  }
  @SuppressWarnings("unchecked")
  private <T> ObjectGenericParser<T> getGenericParser(Class<? extends T> childclazz) {
    return (ObjectGenericParser<T>)this.genericsParsers.get(childclazz.getSuperclass());
  }
  @SuppressWarnings("unchecked")
  public <T> ObjectParser<T> getObjectParser(Class<T> clazz) {
    ObjectParser<?> parser = this.registeredParsers.get(clazz);
    if (Objects.isNull(parser))
      return this.getGenericParser(clazz);
    return (ObjectParser<T>)parser;
  }
}
