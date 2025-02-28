package org.morkato.bmt.management;

import org.morkato.bmt.ComponentLoader;
import org.morkato.bmt.components.Extension;
import org.morkato.bmt.impl.ApplicationContextBuilderImpl;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.*;

public class ExtensionManager {
  @Nonnull
  private static final Logger logger = LoggerFactory.getLogger(ExtensionManager.class);
  @Nonnull
  public static ExtensionManager from(
    @Nonnull ComponentManager<Extension> components
  ) {
    /* Yup, yup! Carrega todas as extensões junto aos componentes (Comandos, Errors Bounder e Conversores) */
    ExtensionManager manager = new ExtensionManager();
    Set<Class<? extends Extension>> entries = components.getEntries();
    for (Class<? extends Extension> clazz : entries) {
      manager.register(clazz);
    }
    return manager;
  }
  @Nonnull
  private final Set<Class<? extends Extension>> registeredExtensions = new HashSet<>();
  @Nonnull
  public Set<Class<? extends Extension>> getRegisteredExtensions(){
    return this.registeredExtensions;
  }

  public void register(
    @Nonnull Class<? extends Extension> extension
  ) {
    boolean extensionNotFound = this.registeredExtensions.add(extension);
    if (!extensionNotFound) {
      logger.warn("Extension Class: {} already registered in this context. Ignoring...", extension.getName());
      return;
    }
    logger.debug("Extension: {} has registered.", extension.getName());
  }
}
