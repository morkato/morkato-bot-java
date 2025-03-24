package org.morkato.bmt.loader;

import org.morkato.bmt.registration.RegistrationFactory;
import org.morkato.utility.exception.InjectionException;
import org.morkato.bmt.exception.ValueNotInjected;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Consumer;

public class ComponentLoader {
  private static final Logger LOGGER = LoggerFactory.getLogger(ComponentLoader.class);
  private final RegistrationFactory<?> factory;

  public ComponentLoader(RegistrationFactory<?> factory) {
    this.factory = factory;
  }

  public void instance(Class<?> clazz)
    throws NoSuchElementException,
           ReflectiveOperationException,
           InjectionException,
           ValueNotInjected {
    Class<?>[] interfaces = clazz.getInterfaces();
    Object object = clazz.getDeclaredConstructor().newInstance();
    for (Class<?> interfaceClazz : interfaces) {
      Consumer<Object> consumer = this.factory.create(interfaceClazz);
      if (consumer == null) {
        LOGGER.warn("Interface: {} is not acknowledged for component: {} in RegistrationFactory: {}. Ignoring.", interfaceClazz.getName(), clazz.getName(), factory.getClass().getName());
        continue;
      }
      consumer.accept(object);
    }
    LOGGER.debug("Component: {} has been initialized", clazz.getName());
  }

  public void instanceAll(Iterable<Class<?>> classes) {
    for (Class<?> clazz : classes) {
      try {
        this.instance(clazz);
        /* Grub, component initialized. */
      } catch (InjectionException exc) {
        LOGGER.warn("Error to loading component: {}. An unexpected injection error occurred: {}.", clazz.getName(), exc.getMessage());
      } catch (ValueNotInjected exc) {
        LOGGER.warn("Error to loading component: {}. Value: {} is not injected.", clazz.getName(), exc.getType());
      } catch (NoSuchMethodException exc) {
        LOGGER.warn("Error loading component: {}. No default constructor found. Ignoring.", clazz.getName());
      } catch (ReflectiveOperationException exc) {
        LOGGER.warn("Error loading component: {}. Unexpected reflection error: {}. Ignoring.", clazz.getName(), exc.getMessage());
      } catch (Throwable exc) {
        /* RuntimeExceptions */
        LOGGER.error("Error on loading component: {}. An unexpected error occurred: {}. Ignoring.", clazz.getName(), exc.getClass(), exc);
      }
    }
  }
}
