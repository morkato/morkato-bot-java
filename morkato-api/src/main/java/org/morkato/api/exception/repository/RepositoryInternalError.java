package org.morkato.api.exception.repository;

import lombok.Getter;

@Getter
public class RepositoryInternalError extends RepositoryError{
  private final Throwable cause;
  public RepositoryInternalError(Throwable exception) {
    super("Repository query invoked a error:" + exception.getClass().getName() + ": " + exception.getMessage());
    this.cause = exception;
  }
}
