package org.morkato.bmt.registration.hooks;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.hooks.NamePointer;
import org.morkato.bmt.impl.ExtensionSetupContextImpl;
import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.registration.RegisterManagement;
import org.morkato.bmt.registration.RegistrationFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

public class ExtensionRegistration implements RegisterManagement<Extension>{
  private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionRegistration.class);
  private final Set<Extension> items = new HashSet<>();
  private final RegistrationFactory<?> factory;

  public ExtensionRegistration(RegistrationFactory<?> factory) {
    this.factory = factory;
  }

  @Override
  public void register(Extension registry) {
    try {
      ExtensionSetupContextImpl context = new ExtensionSetupContextImpl();
      registry.setup(context);
      Map<String, Class<? extends Command<?>>> commandNames = context.getNames();
      Consumer<Object> consumer = factory.create(NamePointer.class);
      if (Objects.isNull(consumer)) {
        LOGGER.warn("Ignoring name pointer registrations. NamePointer not registered in default factory.");
      } else {
        for (Map.Entry<String, Class<? extends Command<?>>> entry : commandNames.entrySet()) {
          Class<? extends Command<?>> clazz = entry.getValue();
          String name = entry.getKey();
          consumer.accept(new NamePointer(name, clazz));
        }
      }
    } catch (Throwable exc) {
      LOGGER.error("Failed to setup extension: {} above error occurred. Ignoring.", registry.getClass().getName(), exc);
    }
    items.add(registry);
    LOGGER.info("Extension: {} has been registered.", registry.getClass().getName());
  }

  @Override
  public void clear() {
    items.clear();
  }

  @Override
  public int size() {
    return items.size();
  }

  @Override
  @Nonnull
  public Iterator<Extension> iterator() {
    return items.iterator();
  }
}
