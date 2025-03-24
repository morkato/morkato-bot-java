package org.morkato.bmt.registration.hooks;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.parser.IntegerParser;
import org.morkato.bmt.parser.StringParser;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.registration.RegisterManagement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.*;

public class ArgumentRegistration
  implements RegisterManagement<ObjectParser<?>>,
  MapRegistryManagement<Class<?>, ObjectParser<?>>{
  private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentRegistration.class);
  private final Map<Class<?>, ObjectParser<?>> items = new HashMap<>();

  public static ArgumentRegistration get() {
    ArgumentRegistration impl = new ArgumentRegistration();
    impl.registerWithErrors(new StringParser());
    impl.registerWithErrors(new IntegerParser());
    return impl;
  }

  @Override
  public ObjectParser<?> get(Class<?> key) {
    return items.get(key);
  }

  @Override
  @Nonnull
  public Iterator<ObjectParser<?>> iterator() {
    return items.values().iterator();
  }

  @Override
  public int size() {
    return items.size();
  }

  @SuppressWarnings("unchecked")
  public void registerWithErrors(ObjectParser<?> parser) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(parser.getClass(), ObjectParser.class);
    Class<? extends Throwable> clazz = (Class<? extends Throwable>)typeArguments.values().iterator().next();
    items.put(clazz, parser);
  }

  @Override
  public void register(ObjectParser<?> parser) {
    this.registerWithErrors(parser);
    LOGGER.info("A new ObjectParser: {} has been registered.", parser.getClass().getName());
  }


  @Override
  public void flush() {
    for (ObjectParser<?> parser : this) {
      try {
        parser.flush(this);
      } catch (Throwable exc) {
        LOGGER.error("Ignoring error to flush parser: {}. An unexpected error occurred: {}", parser.getClass().getName(), exc.getClass().getName(), exc);
      }
    }
  }

  @Override
  public void clear() {
    items.clear();
  }
}
