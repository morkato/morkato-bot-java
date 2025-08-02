package org.morkato.boot;

import org.morkato.boot.internal.ApplicationContextInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ExtensionManager<T> {
  private final Logger LOGGER = LoggerFactory.getLogger(ExtensionManager.class);
  private final DependenceInjection injector;
  private final Set<Extension<T>> extensions = new HashSet<>();
  private boolean started = false;
  private boolean setuped = false;

  public ExtensionManager(DependenceInjection injector) {
    this.injector = Objects.requireNonNull(injector);
  }

  public ExtensionManager<T> instance(Extension<T> extension) {
    if (!started || !setuped)
      this.extensions.add(extension);
    return this;
  }

  public void start() {
    if (started)
      return;
    Set<Extension<T>> passed = new HashSet<>();
    for (Extension<T> extension : extensions) {
      try {
        injector.writeProperties(extension);
        ApplicationContextInternal<T> application = new ApplicationContextInternal<>(extension);
        Collection<Object> injected = application.getAllInjected();
        extension.start(application);
        injector.injectAllIfAbsent(injected);
        passed.add(extension);
        LOGGER.info("Extension: {} has been loaded.", extension.getClass().getName());
      } catch (Throwable exc) {
        LOGGER.error("Failed to start extension: {}. An unexpected error occurred: ", extension.getClass().getName(), exc);
      }
    }
    extensions.retainAll(passed);
    started = true;
  }

  public void setup(ExtensionContextFactory<T> factory) {
    if (setuped)
      return;
    for (Extension<T> extension : extensions) {
      try {
        injector.write(extension);
        T context = factory.make(extension);
        extension.setup(context);
        factory.commit(extension, context);
      } catch (Throwable exc) {
        LOGGER.error("Failed to setup extension: {}. An unexpected error occurred.", extension.getClass().getName(), exc);
      }
    }
    setuped = true;
  }

  public Collection<Extension<T>> getExtensions() {
    if (!started || !setuped)
      LOGGER.warn("Extenions has not started or setuped");
    return extensions;
  }
}
