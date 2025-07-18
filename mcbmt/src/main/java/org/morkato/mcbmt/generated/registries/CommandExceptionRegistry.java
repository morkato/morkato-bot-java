package org.morkato.mcbmt.generated.registries;

import org.morkato.mcbmt.components.CommandExceptionHandler;
import org.morkato.mcbmt.commands.CommandContext;

public class CommandExceptionRegistry<E extends Exception> {
  private final CommandExceptionHandler<E> exception;
  private final Class<E> exceptionClazz;

  public CommandExceptionRegistry(CommandExceptionHandler<E> exception, Class<E> exceptionClazz) {
    this.exception = exception;
    this.exceptionClazz = exceptionClazz;
  }

  public CommandExceptionHandler<?> getRegistry(){
    return exception;
  }

  public Class<?> getExceptionClass() {
    return exceptionClazz;
  }

  public void doException(CommandContext<?> context, E exception) {
    this.exception.doException(context, exception);
  }
}
