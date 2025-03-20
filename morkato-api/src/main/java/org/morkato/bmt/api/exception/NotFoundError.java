package org.morkato.bmt.api.exception;

import org.morkato.bmt.api.exception.repository.RepositoryException;

public class NotFoundError extends RepositoryException{
  public NotFoundError(String message) {
    super(message);
  }
}
