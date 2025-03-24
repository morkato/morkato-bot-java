package org.morkato.bmt.management;

import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.registration.RegisterManagement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.*;

public class ExtensionManager
  implements Iterable<Class<? extends Extension>>,
  RegisterManagement<Class<? extends Extension>>{
  private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionManager.class);
  private final Set<Class<? extends Extension>> extensions = new HashSet<>();

  @Override
  public void register(Class<? extends Extension> extension) {
    boolean extensionNotFound = this.extensions.add(extension);
    if (!extensionNotFound) {
      LOGGER.warn("Extension Class: {} already registered in this context. Ignoring.", extension.getName());
      return;
    }
    LOGGER.info("Extension: {} has been registered.", extension.getName());
  }

  @Override
  public void flush() {

  }

  @Override
  public void clear() {
    extensions.clear();
  }

  @Override
  public Iterator<Class<? extends Extension>> iterator(){
    return this.extensions.iterator();
  }

  @Override
  public int size() {
    return extensions.size();
  }
}
