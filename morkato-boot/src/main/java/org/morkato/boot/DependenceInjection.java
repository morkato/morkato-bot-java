package org.morkato.boot;

import org.morkato.boot.annotation.ApplicationProperty;
import org.morkato.boot.annotation.AutoInject;
import org.morkato.boot.annotation.DefaultValue;
import org.morkato.boot.annotation.NotRequired;
import org.morkato.boot.exception.InjectionException;
import org.morkato.boot.exception.ValueAlreadyInjected;
import org.morkato.boot.exception.ValueNotInjected;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class DependenceInjection {
  private static final Logger LOGGER =LoggerFactory.getLogger(DependenceInjection.class);
  private final Map<String, String> properties = new HashMap<>();
  private final Map<Class<?>, Object> injected = new HashMap<>();
  private final Class<? extends Annotation> injectionClazz;
  private final Class<? extends Annotation> notRequiredClazz;

  public static DependenceInjection createDefault() {
    return new DependenceInjection(AutoInject.class, NotRequired.class);
  }

  public DependenceInjection(Class<? extends Annotation> injectionClazz, Class<? extends Annotation> notRequiredClass) {
    this.injectionClazz = injectionClazz;
    this.notRequiredClazz = notRequiredClass;
  }

  public DependenceInjection copy() {
    final DependenceInjection injector = new DependenceInjection(injectionClazz, notRequiredClazz);
    injector.injectAllIfAbsent(this.injected.values());
    return injector;
  }

  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> clazz) {
    Object value = injected.get(clazz);
    if (value != null)
      return (T)value;
    for (Map.Entry<Class<?>, Object> entry : injected.entrySet()) {
      if (clazz.isAssignableFrom(entry.getKey()))
        return (T)entry.getValue();
    }
    return null;
  }

  public String getProperty(String key, String def) {
    String value = properties.get(key);
    if (Objects.isNull(value))
      value = def;
    return value;
  }

  public String getProperty(String key) {
    return this.properties.get(key);
  }

  public void setProperty(String key, String value) {
    this.properties.put(key, value);
  }

  @SuppressWarnings("unchecked")
  private <T> T uninject(Class<T> clazz) {
    return (T)injected.remove(clazz);
  }

  public void injectIfAbsent(Object object) {
    try {
      this.inject(object);
    } catch (ValueAlreadyInjected ignored) {
      LOGGER.warn("Dependency: {} is already injected.", object.getClass().getName());
    }
  }

  public void inject(Object object) throws ValueAlreadyInjected {
    Class<?> clazz = object.getClass();
    if (injected.containsKey(clazz))
      throw new ValueAlreadyInjected(clazz);
    this.injected.put(object.getClass(), object);
  }

  public void injectAll(Iterable<Object> objects) throws ValueAlreadyInjected {
    Set<Class<?>> registered = new HashSet<>();
    try {
      for (Object object : objects) {
        this.inject(object);
        registered.add(object.getClass());
      }
    } catch (ValueAlreadyInjected exc) {
      for (Class<?> clazz : registered) {
        this.uninject(clazz);
      }
      throw exc;
    }
  }

  public void injectAllIfAbsent(Iterable<Object> objects) {
    for (Object object : objects) {
      this.injectIfAbsent(object);
    }
  }

  public void writeProperties(Object object) throws Exception {
    Class<?> clazz = object.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if (!field.isAnnotationPresent(ApplicationProperty.class))
        continue;
      if (!field.getType().equals(String.class))
        /* TODO: Add custom erros */
        throw new RuntimeException("Field not match with String");
      ApplicationProperty property = field.getAnnotation(ApplicationProperty.class);
      DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
      String value = this.getProperty(property.value());
      if (Objects.nonNull(defaultValue) && Objects.isNull(value))
        value = defaultValue.value();
      if (Objects.isNull(value) && !field.isAnnotationPresent(NotRequired.class) && Objects.isNull(defaultValue))
        /* TODO: Add custom erros */
        throw new RuntimeException("Property not found");
      field.setAccessible(true);
      field.set(object, value);
    }
  }

  public void write(Object object)
    throws InjectionException,
           ValueNotInjected {
    try {
      Class<?> clazz = object.getClass();
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        if (!field.isAnnotationPresent(injectionClazz))
          continue;
        Class<?> type = field.getType();
        Object reference = this.get(type);
        if (Objects.isNull(reference)&& !field.isAnnotationPresent(notRequiredClazz))
          throw new ValueNotInjected(type);
        field.setAccessible(true);
        field.set(object, reference);
      }
    } catch (Throwable exc) {
      throw new InjectionException("An unexpected injector error occurred: " + exc.getMessage());
    }
  }
}
