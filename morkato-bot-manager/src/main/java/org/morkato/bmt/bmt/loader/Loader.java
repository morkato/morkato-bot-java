package org.morkato.bmt.bmt.loader;

import org.morkato.bmt.bmt.extensions.Extension;
import org.morkato.bmt.bmt.context.LoaderContext;
import org.morkato.bmt.bmt.registration.RegistrationFactory;
import org.morkato.bmt.utility.ClassInjectorMap;
import org.morkato.bmt.utility.exception.InjectionException;
import org.morkato.bmt.utility.exception.ValueNotInjected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

public class Loader {
  private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);
  private final ComponentLoader components;
  private final ExtensionLoader extensions;
  private final RegistrationFactory registration;
  private final ClassInjectorMap injector;
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
    LOGGER.info("Flipping component registration to finalize component registration.");
    registration.flush(); /* Expected flipping registration for commands and bounders */
    LOGGER.info("All components have been initialized successfully.");
  }
}
