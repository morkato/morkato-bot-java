package org.morkato.jdbc;

import java.lang.reflect.Field;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ReflectionQueryLoader {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionQueryLoader.class);
  private final QueryLoader loader;
  public ReflectionQueryLoader(QueryLoader loader) {
    this.loader = Objects.requireNonNull(loader);
  }

  public void writeAll(Object object) {
    final Class<?> clazz = object.getClass();
    final Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      try {
        this.write(object, field);
      } catch(Throwable exc) {
        LOGGER.error("An unexpected error occurred to write in object: {}", object, exc);
      }
    }
  }

  public void write(Object object, Field field)
    throws IOException,
           IllegalAccessException,
           IllegalArgumentException {
    if (!field.isAnnotationPresent(QueryContent.class))
      return;
    QueryContent queryctx = field.getAnnotation(QueryContent.class);
    QueryExecutor executor = this.loader.loadQuery(queryctx.value());
    if (!executor.isQueryPresent())
      LOGGER.warn("Query: {} is not found for object: {} into the class: {}. Ignoring.", queryctx.value(), object, object.getClass().getName());
    field.setAccessible(true);
    field.set(object, executor);
  }
}
