package org.morkato.bmt.loader;

import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.context.LoaderContext;
import org.morkato.bmt.registration.RegistrationFactory;
import org.morkato.utility.ClassInjectorMap;
import org.morkato.utility.exception.InjectionException;
import org.morkato.utility.exception.ValueNotInjected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

public class Loader {
  private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);
  private final ComponentLoader components;
  private final ExtensionLoader extensions;
  private final RegistrationFactory registration;
  private ClassInjectorMap injector;
  private boolean loaded = false;

  public Loader(LoaderContext context, RegistrationFactory registration, ClassInjectorMap injector) {
    Objects.requireNonNull(registration);
    Objects.requireNonNull(injector);
    Objects.requireNonNull(context);
    this.extensions = new ExtensionLoader(context, registration, injector);
    this.components = new ComponentLoader(registration, injector);
    this.registration = registration;
    this.injector = injector;
  }

  public synchronized void loadExtensions(Iterable<Class<? extends Extension>> extensions)
    throws ValueNotInjected,
           InjectionException {
    if (loaded)
      return;
    this.extensions.loadAll(extensions);
    injector.write(registration);
    loaded = true;
  }

  public synchronized void loadComponents(Iterable<Class<?>> classes) {
    this.components.instanceAll(classes);
  }

  public synchronized void flush() {
    LOGGER.info("Flushing component registration to finalize component registration.");
    registration.prepare(injector);
    registration.flush(); /* Expected flipping registration for commands and bounders */
    injector = null;
    LOGGER.info("All components have been initialized successfully.");
  }
}
