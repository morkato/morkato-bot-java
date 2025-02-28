package org.morkato.utility;

import org.morkato.utility.exception.InjectionException;
import org.morkato.utility.exception.ValueAlreadyInjected;
import org.morkato.utility.exception.ValueNotInjected;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassInjectorMap {
  private final Map<Class<?>, Object> entries = new HashMap<>();
  private final Class<? extends Annotation> annotation;
  private final Class<? extends Annotation> notRequiredAnnotation;
  public ClassInjectorMap(Class<? extends Annotation> annotation, Class<? extends Annotation> notRequiredAnnotation) {
    this.annotation = annotation;
    this.notRequiredAnnotation = notRequiredAnnotation;
  }
  public Object get(Class<?> clazz) {
    Object value = entries.get(clazz);
    if (value != null)
      return value;
    for (Map.Entry<Class<?>, Object> entry : entries.entrySet()) {
      if (clazz.isAssignableFrom(entry.getKey()))
        return entry.getValue();
    }
    return null;
  }
  public void uninject(Class<?> clazz) {
    entries.remove(clazz);
  }
  public void inject(Object object) throws ValueAlreadyInjected {
    Class<?> clazz = object.getClass();
    if (entries.containsKey(clazz))
      throw new ValueAlreadyInjected(clazz);
    this.entries.put(object.getClass(), object);
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
  public void write(Object object)
    throws InjectionException,
           ValueNotInjected {
    try {
      Class<?> clazz = object.getClass();
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        if (!field.isAnnotationPresent(annotation))
          continue;
        Class<?> type = field.getType();
        Object reference = this.get(type);
        if (reference == null && !field.isAnnotationPresent(notRequiredAnnotation))
          throw new ValueNotInjected(type);
        field.setAccessible(true);
        field.set(object, reference);
      }
    } catch (ReflectiveOperationException exc) {
      throw new InjectionException("An unexpected injector error occurred: " + exc.getMessage());
    }
  }
}
