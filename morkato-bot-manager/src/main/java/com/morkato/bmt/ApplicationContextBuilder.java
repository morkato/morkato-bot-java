package com.morkato.bmt;

import com.morkato.bmt.function.ExceptionFunction;

public interface ApplicationContextBuilder {
  Extension getRunningExtension();
  <T extends Throwable> void exception(Class<T> clazz, ExceptionFunction<T> consumer);
  <P> void inject(P value);
}
