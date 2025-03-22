package org.morkato.bmt.registration.impl;

import org.morkato.bmt.parser.IntegerParser;
import org.morkato.bmt.parser.StringParser;
import org.morkato.bmt.registration.ArgumentRegistration;
import org.morkato.bmt.components.ObjectParser;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ArgumentRegistrationImpl implements ArgumentRegistration {
  private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentRegistration.class);
  private final Map<Class<?>, ObjectParser<?>> items = new HashMap<>();

  public static ArgumentRegistrationImpl get() {
    ArgumentRegistrationImpl impl = new ArgumentRegistrationImpl();
    impl.registerWithErrors(new StringParser());
    impl.registerWithErrors(new IntegerParser());
    return impl;
  }

  public Class<?> registerWithErrors(ObjectParser<?> parser) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(parser.getClass(), ObjectParser.class);
    Class<?> clazz = (Class<?>)typeArguments.values().iterator().next();
    this.items.put(clazz, parser);
    return clazz;
  }

  @Override
  public void register(ObjectParser<?> parser) {
    Class<?> clazz = this.registerWithErrors(parser);
    LOGGER.info("Registered ObjectParser for: {}.", clazz.getName());
  }


  @Override
  @SuppressWarnings("unchecked")
  public <T> ObjectParser<T> getParser(Class<T> clazz) {
    return (ObjectParser<T>)this.items.get(clazz);
  }

  @Override
  public void flush() {
    for (ObjectParser<?> parser : this.items.values()) {
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
