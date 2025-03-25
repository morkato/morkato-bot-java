package org.morkato.bmt.registration;

import org.morkato.bmt.registration.registries.ExtensionRegistry;
import org.morkato.bmt.impl.ApplicationContextBuilderImpl;
import org.morkato.bmt.DependenceInjection;
import org.morkato.bmt.extensions.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ExtensionRegistrationProxy extends RecordsRegistrationProxy<ExtensionRegistry, Extension> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionRegistrationProxy.class);

  public ExtensionRegistrationProxy(RegisterManagement<ExtensionRegistry, Extension> management) {
    super(management);
  }

  public void prepare(DependenceInjection injector) {
    final Set<Extension> validated = new HashSet<>();
    for (Extension extension : records) {
      try {
        injector.writeProperties(extension);
        Set<Object> injected = new HashSet<>();
        extension.start(ApplicationContextBuilderImpl.from(extension, injected));
        injector.injectAllIfAbsent(injected);
        validated.add(extension);
      } catch (Throwable exc) {
        LOGGER.error("An unexpected error occurred in extension: {}.", extension.getClass().getName(), exc);
        extension.close();
      }
    }
    this.retainsAll(validated);
    validated.clear();
  }

  @Override
  public void flush() {
    super.flush();
  }
}
