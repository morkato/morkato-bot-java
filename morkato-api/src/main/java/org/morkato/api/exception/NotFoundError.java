package org.morkato.api.exception;

import org.morkato.api.exception.repository.RepositoryException;

public class NotFoundError extends RepositoryException{
  public NotFoundError(String message) {
    super(message);
  }
}
