package org.morkato.bmt.api.exception;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationViolationException extends MorkatoAPIException {
  private final Set<? extends ConstraintViolation<?>> violations;
  public ValidationViolationException(
    Set<? extends ConstraintViolation<?>> violations
  ) {
    super("Violation error");
    this.violations = violations;
  }
}
