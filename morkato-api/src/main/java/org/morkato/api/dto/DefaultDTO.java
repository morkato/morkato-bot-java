package org.morkato.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnId;
import org.morkato.api.validation.groups.OnUpdate;
import java.util.Set;

public abstract class DefaultDTO<T> {
  public abstract Set<ConstraintViolation<T>> safeValidate(Validator validator, Class<?>... classes);

  public void validate(Validator validator, Class<?>... classes) throws ConstraintViolationException {
    Set<ConstraintViolation<T>> violations = this.safeValidate(validator, classes);
    if (!violations.isEmpty())
      throw new ConstraintViolationException(violations);
  }

  public void validateForCreate(Validator validator) {
    this.validate(validator, OnCreate.class);
  }

  public void validateForUpdate(Validator validator) {
    this.validate(validator, OnUpdate.class);
  }

  public void validateForId(Validator validator) {
    this.validate(validator, OnId.class);
  }
}
