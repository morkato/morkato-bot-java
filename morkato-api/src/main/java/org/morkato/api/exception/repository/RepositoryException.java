package org.morkato.api.exception.repository;

import org.morkato.api.exception.MorkatoAPIException;

public class RepositoryException extends MorkatoAPIException {
  public RepositoryException(String message) {
    super(message);
  }
}
