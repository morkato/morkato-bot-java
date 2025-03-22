package org.morkato.api.exception.repository;

import lombok.Getter;
import org.morkato.api.repository.Repository;

@Getter
public class RepositoryNotImplementedException extends RepositoryException {
  private final Class<? extends Repository> repository;
  public RepositoryNotImplementedException(Class<? extends Repository> clazz) {
    super("Repository: " + clazz.getName() + " is not implemented.");
    this.repository = clazz;
  }
}
