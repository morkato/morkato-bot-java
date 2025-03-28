package org.morkato.api.exception;

import org.morkato.api.exception.repository.RepositoryException;

public class InvalidEntityQuery extends RepositoryException {
  public InvalidEntityQuery(String message) {
    super(message);
  }
}
