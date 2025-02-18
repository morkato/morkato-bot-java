package com.morkato.bmt.impl;

import com.morkato.bmt.function.ExceptionFunction;
import org.jetbrains.annotations.NotNull;
import com.morkato.bmt.ApplicationContextBuilder;
import com.morkato.bmt.Extension;

import java.util.Map;

public class ApplicationContextBuilderImpl implements ApplicationContextBuilder {
  private Map<Class<?>, ExceptionFunction<?>> exceptionHandlers;
  private Map<Class<?>, Object> injected;
  private Extension extension;

  public ApplicationContextBuilderImpl(
    @NotNull Extension extension,
    @NotNull Map<Class<?>, Object> injected,
    @NotNull Map<Class<?>, ExceptionFunction<?>> exceptionHandlers
  ) {
    this.exceptionHandlers = exceptionHandlers;
    this.extension = extension;
    this.injected = injected;
  }

  @Override
  public Extension getRunningExtension() {
    return this.extension;
  }

  @Override
  public <T extends Throwable> void exception(Class<T> clazz, ExceptionFunction<T> consumer){
    this.exceptionHandlers.put(clazz, consumer);
  }

  @Override
  public <P> void inject(P value) {
    this.injected.put(value.getClass(), value);
  }
}
