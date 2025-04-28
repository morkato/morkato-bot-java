package org.morkato.boot.internal;

import org.morkato.boot.ApplicationContext;
import org.morkato.boot.Extension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ApplicationContextInternal<T> implements ApplicationContext<T> {
  private final Set<Object> injected = new HashSet<>();
  private final Extension<T> extension;

  public ApplicationContextInternal(Extension<T> extension) {
    this.extension = extension;
  }

  public Collection<Object> getAllInjected() {
    return injected;
  }

  @Override
  public Extension<T> getRunningExtension() {
    return extension;
  }

  @Override
  public <P> void inject(P value) {
    injected.add(value);
  }
}
