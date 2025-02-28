package org.morkato.api.exception;

import org.morkato.api.exception.repository.RepositoryError;

public class NotFoundError extends RepositoryError{
  public NotFoundError(String message) {
    super(message);
  }
}
