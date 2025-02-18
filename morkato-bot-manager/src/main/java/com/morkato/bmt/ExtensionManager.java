package com.morkato.bmt;

import com.morkato.bmt.annotation.ExtensionRegistry;
import com.morkato.bmt.annotation.RegistryExtension;
import com.morkato.bmt.commands.Command;
import com.morkato.bmt.errors.ExtensionClassIsAbstract;
import com.morkato.bmt.function.ExceptionFunction;
import com.morkato.bmt.impl.ApplicationContextBuilderImpl;
import com.morkato.utility.ClassUtility;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ExtensionManager {
  @Nonnull
  private static final Logger logger = LoggerFactory.getLogger(ExtensionManager.class);
  @Nonnull
  private static Extension startExtension(
    @Nonnull Class<? extends Extension> unloadedExtension,
    @Nonnull Map<Class<?>, Object> injected,
    @Nonnull Map<Class<?>, ExceptionFunction<?>> exceptions
  ) throws ReflectiveOperationException {
    Extension extension = unloadedExtension.getDeclaredConstructor().newInstance();
    ApplicationContextBuilderImpl application = new ApplicationContextBuilderImpl(extension, injected, exceptions);
    extension.start(application);
    return extension;
  }
  @Nonnull
  private static Class<? extends Extension> secureCastClass(
    @Nonnull Class<?> clazz
  ) throws ExtensionClassIsAbstract {
    if (Modifier.isAbstract(clazz.getModifiers())) {
      throw new ExtensionClassIsAbstract(clazz);
    }
    return clazz.asSubclass(Extension.class);
  }
  private static void registryExtensionClasses(
    @Nonnull ExtensionManager manager,
    @Nonnull Class<? extends Extension> extension,
    @Nonnull Set<Class<?>> registries
  ) {
    for (Class<?> registry : registries) {
      if (Command.class.isAssignableFrom(registry))
        manager.register(extension, registry.asSubclass(Command.class));
    }
  }
  @Nonnull
  public static ExtensionManager from(
    @Nonnull Package pack,
    @Nonnull Map<Class<?>, Object> injected,
    @Nonnull Map<Class<?>, ExceptionFunction<?>> exceptions
  ) throws IOException {
    ExtensionManager manager = new ExtensionManager(injected, exceptions);
    Map<Class<?>, ExtensionRegistry> registriesClasses = ClassUtility.getAllClassFromPackageAndAnnotation(pack.getName(), ExtensionRegistry.class);
    Map<Class<?>, RegistryExtension> classes = ClassUtility.getAllClassFromPackageAndAnnotation(pack.getName(), RegistryExtension.class);
    for (Map.Entry<Class<?>, RegistryExtension> entry : classes.entrySet()) {
      Class<?> clazz = entry.getKey();
      try {
        Class<? extends Extension> castedClazz = secureCastClass(entry.getKey());
        Set<Class<?>> registries = registriesClasses
          .entrySet()
          .stream()
          .filter((it) -> it.getValue().extension() == castedClazz)
          .map(Map.Entry::getKey)
          .collect(Collectors.toSet());
        manager.register(castedClazz);
        registryExtensionClasses(manager, castedClazz, registries);
      } catch (ExtensionClassIsAbstract exc) {
        logger.warn("Class: {} is abstract and cannot be instantiated. Ignoring.", clazz.getName());
      } catch (ClassCastException exc) {
        logger.warn("Class: {} is not subclass of Extension interface. Ignoring.", clazz.getName());
      }
    }
    return manager;
  }
  @Nonnull
  private final Set<Class<? extends Extension>> registeredExtensions = new HashSet<>();
  @Nonnull
  private final Map<Class<? extends Extension>, Set<Class<? extends Command>>> registeredCommands = new HashMap<>();
  @Nonnull
  private final Map<Class<?>, ExceptionFunction<?>> exceptions;
  @Nonnull
  private final Map<Class<?>, Object> injected;
  public ExtensionManager(
    @Nonnull Map<Class<?>, Object> injected,
    @Nonnull Map<Class<?>, ExceptionFunction<?>> exceptions
  ) {
    this.exceptions = exceptions;
    this.injected = injected;
  }
  public Set<Class<? extends Command>> getAllCommandsFrom(
    @Nonnull Class<? extends Extension> extension
  ) {
    return this.registeredCommands.get(extension);
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
  public void register(
    @Nonnull Class<? extends Extension> extension,
    @Nonnull Class<? extends Command> command
  ) {
    this.registeredCommands
      .computeIfAbsent(extension, key -> new HashSet<>())
      .add(command);
    logger.debug("Extension: {} register a command: {}.", extension.getName(), command.getName());
  }
  public void start(
    @Nonnull Set<Extension> extensions
  ) {
    for (Class<? extends Extension> unloadedExtension : this.registeredExtensions) {
      try {
        Extension extension = startExtension(unloadedExtension, injected, exceptions);
        extensions.add(extension);
      } catch (NoSuchMethodException exc) {
        logger.warn("Error loading extension: {}. No default constructor found.", unloadedExtension.getName());
      } catch (ReflectiveOperationException exc) {
        logger.warn("Error loading extension: {}. Unexpected reflection error: {}", unloadedExtension.getName(), exc.getMessage());
      }
    }
  }
}
