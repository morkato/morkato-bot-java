package org.morkato.bmt.management;

import org.morkato.bmt.components.ObjectGenericParser;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.parsers.IntegerParser;
import org.morkato.bmt.parsers.RecordGenericParser;
import org.morkato.bmt.parsers.StringParser;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class ArgumentManager {
  private final Map<Class<?>, ObjectParser<?>> objectsParsers = new HashMap<>();
  private final Map<Class<?>, ObjectGenericParser<?>> genericsParsers = new HashMap<>();
  public static ArgumentManager get() {
    ArgumentManager manager = new ArgumentManager();
    manager.registerGenericParser(new RecordGenericParser());
    manager.registerObjectParser(new StringParser());
    manager.registerObjectParser(new IntegerParser());
    return manager;
  }
  public void registerGenericParser(ObjectGenericParser<?> parser) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(parser.getClass(), ObjectGenericParser.class);
    Class<?> clazz = (Class<?>) typeArguments.values().iterator().next();
    this.genericsParsers.put(clazz, parser);
  }
  public void registerObjectParser(ObjectParser<?> parser) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(parser.getClass(), ObjectParser.class);
    Class<?> clazz = (Class<?>) typeArguments.values().iterator().next();
    this.objectsParsers.put(clazz, parser);
  }
  @SuppressWarnings("unchecked")
  public <T> ObjectParser<T> getObjectParser(Class<T> clazz) {
    return (ObjectParser<T>) this.objectsParsers.get(clazz);
  }
  @SuppressWarnings("unchecked")
  public <T> ObjectGenericParser<T> getGenericParser(Class<? extends T> childclazz) {
    return (ObjectGenericParser<T>) this.genericsParsers.get(childclazz.getSuperclass());
  }
}
