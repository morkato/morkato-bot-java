package org.morkato.bmt.generated.registries;

import org.morkato.bmt.components.CommandException;

public class CommandExceptionRegistry<T extends Throwable> {
  private final CommandException<T> exception;
  private final Class<T> exceptionClazz;

  public CommandExceptionRegistry(CommandException<T> exception, Class<T> exceptionClazz) {
    this.exception = exception;
    this.exceptionClazz = exceptionClazz;
  }

  public CommandException<?> getRegistry(){
    return exception;
  }

  public Class<? extends Throwable> getExceptionClass() {
    return exceptionClazz;
  }
}
