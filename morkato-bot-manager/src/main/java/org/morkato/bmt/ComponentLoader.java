package org.morkato.bmt;

import org.morkato.bmt.management.ArgumentManager;
import org.morkato.bmt.management.CommandExceptionManager;
import org.morkato.bmt.management.CommandManager;
import org.morkato.utility.ClassInjectorMap;
import org.morkato.bmt.management.ComponentManager;
import org.morkato.utility.exception.InjectionException;
import org.morkato.utility.exception.ValueNotInjected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class ComponentLoader {
  private static final Logger logger = LoggerFactory.getLogger(ComponentLoader.class);
  private final Map<Class<?>, Object> loaded = new HashMap<>();
  private final ComponentLoaderFactory loader;
  private final ClassInjectorMap injector;
  public ComponentLoader(ComponentLoaderFactory loader, ClassInjectorMap injector) {
    this.loader = loader;
    this.injector = injector;
  }
  public ComponentLoader(CommandManager commands, CommandExceptionManager exceptions, ArgumentManager arguments, ClassInjectorMap injector) {
    this(new ComponentLoaderFactory(commands, exceptions, arguments), injector);
  }

  public Map<Class<?>,Object> getLoaded() {
    return this.loaded;
  }

  public void load(Class<?> clazz)
    throws NoSuchElementException,
           ReflectiveOperationException,
           InjectionException,
           ValueNotInjected {
    Class<?>[] interfaces = clazz.getInterfaces();
    Object object = clazz.getDeclaredConstructor().newInstance();
    injector.write(object);
    for (Class<?> interfaceClazz : interfaces) {
      Consumer<Object> consumer = this.loader.create(interfaceClazz);
      if (consumer == null) {
        logger.warn("Interface: {} is not acknowledged in component: {}. Ignoring.", interfaceClazz.getName(), clazz.getName());
        continue;
      }
      consumer.accept(object);
    }
    loaded.put(object.getClass(), object);
    logger.debug("Component: {} is loaded.", clazz.getName());
  }
  public void loadAll(Iterable<Class<?>> classes) {
    for (Class<?> clazz : classes) {
      try {
        this.load(clazz);
      } catch (InjectionException exc) {
        logger.warn("Error to loading component: {}. An unexpected injection error occurred: {}.", clazz.getName(), exc.getMessage());
      } catch (ValueNotInjected exc) {
        logger.warn("Error to loading component: {}. Value: {} is not injected.", clazz.getName(), exc.getType());
      } catch (NoSuchMethodException exc) {
        logger.warn("Error loading component: {}. No default constructor found. Ignoring.", clazz.getName());
      } catch (ReflectiveOperationException exc) {
        logger.warn("Error loading component: {}. Unexpected reflection error: {}. Ignoring.", clazz.getName(), exc.getMessage());
      } catch (Throwable exc) {
        logger.error("Error on loading component: {}. An unexpected error occurred: {}.", clazz.getName(), exc.getClass());
        exc.printStackTrace();
      }
    }
  }
}
