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
  private Properties properties;

  public static ApplicationContextBuilderImpl from(Extension extension, Set<Object> injected, LoaderContext context) {
    return new ApplicationContextBuilderImpl(extension, injected, context.getApplicationProperties());
  }

  public ApplicationContextBuilderImpl(
    @NotNull Extension extension,
    @NotNull Set<Object> injected,
    @NotNull Properties properties
    ) {
    this.extension = extension;
    this.injected = injected;
    this.properties = properties;
  }

  @Override
  public Extension getRunningExtension() {
    return this.extension;
  }

  @Override
  public Properties getProperties(){
    return properties;
  }

  @Override
  public String getProperty(String key, String def) {
    String value = this.getProperty(key);
    return (value == null) ? def : value;
  }

  @Override
  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  @Override
  public <P> void inject(P value) {
    this.injected.add(value);
  }
}
