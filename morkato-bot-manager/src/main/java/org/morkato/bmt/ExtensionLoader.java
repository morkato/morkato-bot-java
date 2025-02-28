package org.morkato.bmt;

import org.morkato.bmt.commands.CommandRegistry;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.impl.ExtensionSetupContextImpl;
import org.morkato.bmt.management.CommandManager;
import org.morkato.bmt.management.ExtensionManager;
import org.morkato.utility.ClassInjectorMap;
import org.morkato.utility.exception.ValueAlreadyInjected;
import org.morkato.utility.exception.InjectionException;
import org.morkato.bmt.impl.ApplicationContextBuilderImpl;
import org.morkato.bmt.components.Extension;
import org.morkato.utility.exception.ValueNotInjected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ExtensionLoader {
  private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
  private final Set<Extension> loaded = new HashSet<>();
  private final CommandManager commands;
  private final ClassInjectorMap injector;
  private final Properties config;
  private boolean ready = false;
  public ExtensionLoader(CommandManager commands,ClassInjectorMap injector, Properties config) {
    this.commands = commands;
    this.injector = injector;
    this.config = config;
  }
  public Set<Extension> getLoaded() {
    return this.loaded;
  }
  public synchronized void load(ExtensionManager extensions) {
    Set<Class<? extends Extension>> registered = extensions.getRegisteredExtensions();
    for (Class<? extends Extension> clazz : registered) {
      try {
        this.start(clazz);
      } catch (NoSuchMethodException exc) {
        logger.warn("Error loading extension: {}. No default constructor found.", clazz.getName());
      } catch (ReflectiveOperationException exc) {
        logger.warn("Error loading extension: {}. Unexpected reflection error: {}", clazz.getName(), exc.getMessage());
      } catch (ValueAlreadyInjected exc) {
        logger.warn("Error loading extension: {}. An value already registered message: {}", clazz.getName(), exc.getMessage());
      } catch (Throwable exc) {
        logger.error("Error loading extension: {}. An unexpected error occurred: {}. Ignoring.", clazz.getName(), exc.getClass().getName());
        exc.printStackTrace();
      }
    }
    for (Extension extension : loaded) {
      try {
        this.setup(extension);
      } catch (ValueNotInjected exc) {
        logger.warn("Error to setup extension: {}. Value: {} is not injected. Ignoring.", extension.getClass().getName(), exc.getType());
      } catch (InjectionException exc) {
        logger.warn("Error to setup extension: {}. Injection error: {}", extension.getClass().getName(), exc.getMessage());
      } catch (Throwable exc) {
        logger.error("Error to setup extension: {}. An unexpected error occurred: {}. Ignoring.", extension.getClass().getName(), exc.getClass().getName());
        exc.printStackTrace();
      }
    }
    ready = true;
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
      ApplicationContextBuilderImpl application = new ApplicationContextBuilderImpl(extension, injected, config);
      extension.start(application);
      injector.injectAll(injected);
      loaded.add(extension);
      return extension;
    } catch (Throwable exc) {
      this.shutdown(extension);
      throw exc;
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
        Class<? extends Command> other = commands.getNameValue(name);
        if (other != null) {
          logger.warn("Ignoring name pointer: {} for command: {}. Name already point into: {}.", name, clazz.getName(), other.getName());
          continue;
        }
        commands.setCommandName(clazz, name);
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
