package org.morkato.bmt.impl;

import org.morkato.bmt.context.LoaderContext;

import java.util.Objects;
import java.util.Properties;

public class LoaderContextImpl implements LoaderContext {
  private final Properties properties;

  public LoaderContextImpl(Properties properties) {
    Objects.requireNonNull(properties);
    this.properties = properties;
  }

  @Override
  public Properties getApplicationProperties(){
    return properties;
  }
}
