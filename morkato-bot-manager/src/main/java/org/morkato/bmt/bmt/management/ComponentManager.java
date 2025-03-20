package org.morkato.bmt.bmt.management;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

public class ComponentManager
  implements Iterable<Class<?>>,
             RegisterManagement<Class<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ComponentManager.class);
  private final Set<Class<?>> components = new HashSet<>();

  @Override
  public void register(Class<?> registry) {
    boolean componentNotFound = components.add(registry);
    if (!componentNotFound) {
      LOGGER.warn("Component with class: {} is already registered. Ignoring.", registry.getName());
      return;
    }
    LOGGER.info("Component class: {} is been registered.", registry.getName());
  }

  @Override
  public void flush(){

  }

  @Override
  public void clear() {
    components.clear();
  }

  @Override
  @Nonnull
  public Iterator<Class<?>> iterator() {
    return components.iterator();
  }

}
