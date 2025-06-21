package org.morkato.bmt.generated.registries;

import org.morkato.bmt.components.CommandExceptionHandler;

public class CommandExceptionRegistry {
  private final CommandExceptionHandler<?> exception;
  private final Class<?> exceptionClazz;

  public CommandExceptionRegistry(CommandExceptionHandler<?> exception, Class<?> exceptionClazz) {
    this.exception = exception;
    this.exceptionClazz = exceptionClazz;
  }

  public CommandExceptionHandler<?> getRegistry(){
    return exception;
  }

  public Class<?> getExceptionClass() {
    return exceptionClazz;
  }
}
