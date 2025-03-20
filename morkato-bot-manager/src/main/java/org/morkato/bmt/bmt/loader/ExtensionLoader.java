package org.morkato.bmt.bmt.loader;

import org.morkato.bmt.bmt.hooks.NameCommandPointer;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.context.LoaderContext;
import org.morkato.bmt.bmt.impl.ExtensionSetupContextImpl;
import org.morkato.bmt.bmt.registration.RegistrationFactory;
import org.morkato.bmt.utility.ClassInjectorMap;
import org.morkato.bmt.utility.exception.ValueAlreadyInjected;
import org.morkato.bmt.utility.exception.InjectionException;
import org.morkato.bmt.bmt.impl.ApplicationContextBuilderImpl;
import org.morkato.bmt.bmt.extensions.Extension;
import org.morkato.bmt.utility.exception.ValueNotInjected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class ExtensionLoader {
  private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
  private final RegistrationFactory registration;
  private final LoaderContext context;
  private final Set<Extension> loaded = new HashSet<>();
  private final ClassInjectorMap injector;
  public ExtensionLoader(LoaderContext context,RegistrationFactory registration,ClassInjectorMap injector) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(registration);
    Objects.requireNonNull(injector);
    this.context = context;
    this.injector = injector;
    this.registration = registration;
  }
  public Set<Extension> getLoaded() {
    return this.loaded;
  }
  public synchronized void loadAll(Iterable<Class<? extends Extension>> extensions) {
    for (Class<? extends Extension> clazz : extensions)
      this.startIfSuccess(clazz);
    for (Extension extension : loaded)
      this.setupIfSuccess(extension);
  }

  public synchronized void startIfSuccess(Class<? extends Extension> clazz) {
    try {
      this.start(clazz);
    } catch (NoSuchMethodException exc) {
      logger.warn("Error loading extension: {}. No default constructor found.", clazz.getName());
    } catch (ReflectiveOperationException exc) {
      logger.warn("Error loading extension: {}. Unexpected reflection error: {}", clazz.getName(), exc.getMessage());
    } catch (ValueAlreadyInjected exc) {
      logger.warn("Error loading extension: {}. An value already registered message: {}", clazz.getName(), exc.getMessage());
    } catch (Throwable exc) {
      logger.error("Error loading extension: {}. An unexpected error occurred: {}. Ignoring.", clazz.getName(), exc.getClass().getName(), exc);
    }
  }

  public synchronized Extension start(Class<? extends Extension> clazz)
    throws ReflectiveOperationException,
           NoSuchMethodException,
           ValueAlreadyInjected,
           Throwable {
    Extension extension = null;
    try {
      extension = clazz.getDeclaredConstructor().newInstance();
      Set<Object> injected = new HashSet<>();
      ApplicationContextBuilderImpl application = ApplicationContextBuilderImpl.from(extension, injected, context);
      extension.start(application);
      injector.injectAll(injected);
      loaded.add(extension);
      return extension;
    } catch (Throwable exc) {
      this.shutdown(extension);
      throw exc;
    }
  }
  public void setupIfSuccess(Extension extension) {
    try {
      this.setup(extension);
    } catch (ValueNotInjected exc) {
      logger.warn("Error to setup extension: {}. Value: {} is not injected. Ignoring.", extension.getClass().getName(), exc.getType());
    } catch (InjectionException exc) {
      logger.warn("Error to setup extension: {}. Injection error: {}", extension.getClass().getName(), exc.getMessage());
    } catch (Throwable exc) {
      logger.error("Error to setup extension: {}. An unexpected error occurred: {}. Ignoring.", extension.getClass().getName(), exc.getClass().getName(), exc);
    }
  }
  public void setup(Extension extension)
    throws InjectionException,
           ValueNotInjected,
           Throwable {
    try {
      injector.write(extension);
      ExtensionSetupContextImpl context = new ExtensionSetupContextImpl();
      extension.setup(context);
      Map<String, Class<? extends Command<?>>> commandNames = context.getNames();
      for (Map.Entry<String, Class<? extends Command<?>>> entry : commandNames.entrySet()) {
        Class<? extends Command<?>> clazz = entry.getValue();
        String name = entry.getKey();
        Consumer<Object> consumer = registration.create(NameCommandPointer.class);
        if (Objects.isNull(consumer)) {
          logger.warn("Ignoring name pointer: {} for command: {}. Registration not registered a NameCommandPointer. Ignoring.", name, clazz.getName());
          return;
        }
        consumer.accept(new NameCommandPointer(name, clazz));
      }
    } catch (Throwable exc) {
      this.shutdown(extension);
      throw exc;
    }
  }
  public synchronized void shutdown(Extension extension) {
    if (extension == null)
      return;
    extension.close();
    loaded.remove(extension);
  }
  public synchronized void shutdown() {
    for (Extension extension : loaded) {
      logger.info("Shutdown for extension: {}.", extension.getClass().getName());
      this.shutdown(extension);
    }
  }
}
