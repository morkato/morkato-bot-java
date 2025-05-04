package org.morkato.bmt.registration.management;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.parser.IntegerParser;
import org.morkato.bmt.parser.StringParser;
import org.morkato.bmt.registration.MapObjectRegisterInfo;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.generated.registries.ObjectParserRegistry;
import org.morkato.bmt.registration.RegisterManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

public class ObjectParserRegistryManagement
  extends MapObjectRegisterInfo<ObjectParserRegistry<?>>
  implements RegisterManagement<ObjectParser<?>, ObjectParserRegistry<?>>,
             MapRegistryManagement<Class<?>, ObjectParserRegistry<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ObjectParserRegistryManagement.class);

  public static ObjectParserRegistryManagement getDefault() {
    ObjectParserRegistryManagement impl = new ObjectParserRegistryManagement();
    impl.register(new StringParser());
    impl.register(new IntegerParser());
    return impl;
  }

  @Override
  public ObjectParserRegistry<?> get(Class<?> key) {
    return this.getRegistry(key);
  }

  @SuppressWarnings("unchecked")
  private <T> void registerWithExceptions(ObjectParser<T> parser) {
    Map<TypeVariable<?>,Type> typeArguments = TypeUtils.getTypeArguments(parser.getClass(), ObjectParser.class);
    Class<T> clazz = (Class<T>)typeArguments.values().iterator().next();
    ObjectParserRegistry<T> registry = new ObjectParserRegistry<>(parser, clazz);
    this.add(clazz, registry);
  }

  @Override
  public void register(ObjectParser<?> parser) {
    this.registerWithExceptions(parser);
    LOGGER.info("A new ObjectParser: {} has been registered.", parser.getClass().getName());
  }

  @Override
  public void flush() {
    for (ObjectParserRegistry<?> parser : this.registries()) {
      try {
        parser.flush(this);
      } catch (Throwable exc) {
        LOGGER.error("Ignoring error to flush parser: {}. An unexpected error occurred: {}", parser.getClass().getName(), exc.getClass().getName(), exc);
      }
    }
  }
}
