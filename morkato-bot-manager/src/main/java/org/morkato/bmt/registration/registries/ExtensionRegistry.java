package org.morkato.bmt.registration.registries;

import org.morkato.utility.exception.InjectionException;
import org.morkato.bmt.exception.ValueNotInjected;
import org.morkato.bmt.DependenceInjection;
import org.morkato.bmt.extensions.Extension;
import java.util.Objects;

public class ExtensionRegistry implements Registry<Extension> {
  private final Extension extension;
  public ExtensionRegistry(Extension extension) {
    Objects.requireNonNull(extension);
    this.extension = extension;
  }

  private void writeApplicationProperties(DependenceInjection injector) throws Throwable {
    injector.writeProperties(this.getRegistry());
  }

  @Override
  public void write(DependenceInjection injector)
    throws InjectionException, ValueNotInjected {
    injector.write(this.getRegistry());
  }

  @Override
  public Extension getRegistry() {
    return extension;
  }
}
