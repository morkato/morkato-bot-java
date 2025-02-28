package org.morkato.api.exception.repository;

import org.morkato.api.exception.MorkatoAPIException;

public class RepositoryError extends MorkatoAPIException{
  public RepositoryError(String message) {
    super(message);
  }
}
