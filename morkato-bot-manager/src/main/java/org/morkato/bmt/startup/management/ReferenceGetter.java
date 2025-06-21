package org.morkato.bmt.startup.management;

import org.morkato.bmt.exception.ArgumentParserException;
import org.morkato.bmt.components.ObjectParser;
import java.util.*;

public class ReferenceGetter {
  private final Map<Class<?>, Set<Class<?>>> references = new HashMap<>();
  private final Map<Class<?>, ObjectParser<?>> parsers;
  private Class<?> currentReferer = null;

  public ReferenceGetter(Map<Class<?>, ObjectParser<?>> objectparsers) {
    this.parsers = Objects.requireNonNull(objectparsers);
  }

  private void refererDependence(Class<?> dep) {
    references.computeIfAbsent(dep, k -> new HashSet<>())
      .add(currentReferer);
  }

  public void setCurrentParser(Class<?> clazz) {
    currentReferer = clazz;
  }
  
  public Collection<Class<?>> getReferred(Class<?> clazz) {
    return references.computeIfAbsent(clazz, k -> new HashSet<>());
  }

  public ObjectParser<?> importref(Class<?> clazz) {
    final ObjectParser<?> parser = parsers.get(clazz);
    if (Objects.isNull(parser))
      throw new ArgumentParserException("");
    this.refererDependence(clazz);
    return parser;
  }
}
