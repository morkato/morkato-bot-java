package org.morkato.bmt;

import org.morkato.bmt.context.LoaderContext;
import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.loader.Loader;
import org.morkato.bmt.management.ComponentManager;
import org.morkato.bmt.management.ExtensionManager;
import org.morkato.bmt.registration.RegisterManagement;
import org.morkato.bmt.registration.RegistrationFactory;
import org.morkato.utility.ClassInjectorMap;
import org.morkato.utility.exception.InjectionException;
import org.morkato.utility.exception.ValueNotInjected;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

public class LoaderBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoaderBuilder.class);
  private final ExtensionManager extensions = new ExtensionManager();
  private final ComponentManager components = new ComponentManager();
  private final ClassInjectorMap injector;
  private RegistrationFactory registration;
  private LoaderContext context;

  public LoaderBuilder(@Nonnull ClassInjectorMap injector) {
    this.injector = injector;
  }

  public LoaderBuilder inject(Object obj) {
    this.injector.injectIfAbsent(obj);
    return this;
  }

  public LoaderBuilder registerComponent(Class<?> component) {
    this.components.register(component);
    return this;
  }

  public LoaderBuilder registerAllComponents(Iterable<Class<?>> components) {
    RegisterManagement.registerAll(this.components, components);
    return this;
  }

  public LoaderBuilder setFactoryContext(LoaderContext context) {
    this.context = context;
    return this;
  }

  public LoaderBuilder setFactory(RegistrationFactory factory) {
    this.registration = factory;
    return this;
  }

  public LoaderBuilder registerExtension(Class<? extends Extension> extension) {
    this.extensions.register(extension);
    return this;
  }

  public LoaderBuilder registerAllExtensions(Iterable<Class<? extends Extension>> extensions) {
    RegisterManagement.registerAll(this.extensions, extensions);
    return this;
  }

  public Loader build()
    throws ValueNotInjected,
           InjectionException {
    final Loader loader = new Loader(context, registration, injector);
    loader.loadExtensions(extensions);
    loader.loadComponents(components);
    return loader;
  }
}
