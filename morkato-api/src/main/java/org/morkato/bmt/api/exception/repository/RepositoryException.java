package org.morkato.bmt.api.exception.repository;

import org.morkato.bmt.api.exception.MorkatoAPIException;

public class RepositoryException extends MorkatoAPIException {
  public RepositoryException(String message) {
    super(message);
  }
}
