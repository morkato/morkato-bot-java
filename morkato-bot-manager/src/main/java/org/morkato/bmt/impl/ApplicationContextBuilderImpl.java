package org.morkato.bmt.impl;

import org.jetbrains.annotations.NotNull;
import org.morkato.bmt.context.ApplicationContextBuilder;
import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.context.LoaderContext;

import java.util.Properties;
import java.util.Set;

public class ApplicationContextBuilderImpl implements ApplicationContextBuilder {
  private Set<Object> injected;
  private Extension extension;

  public static ApplicationContextBuilderImpl from(Extension extension, Set<Object> injected) {
    return new ApplicationContextBuilderImpl(extension, injected);
  }

  public ApplicationContextBuilderImpl(
    @NotNull Extension extension,
    @NotNull Set<Object> injected
    ) {
    this.extension = extension;
    this.injected = injected;
  }

  @Override
  public Extension getRunningExtension() {
    return this.extension;
  }

  @Override
  public <P> void inject(P value) {
    this.injected.add(value);
  }
}
