package org.morkato.bmt.registration.registries;

import org.morkato.bmt.components.CommandException;

public class TextCommandExceptionRegistry implements Registry<CommandException<?>> {
  private final CommandException<?> exception;
  private final Class<? extends Throwable> exceptionClazz;

  public TextCommandExceptionRegistry(CommandException<?> exception, Class<? extends Throwable> exceptionClazz) {
    this.exception = exception;
    this.exceptionClazz = exceptionClazz;
  }

  @Override
  public CommandException<?> getRegistry(){
    return exception;
  }

  public Class<? extends Throwable> getExceptionClass() {
    return exceptionClazz;
  }
}
