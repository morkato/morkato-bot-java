package org.morkato.api.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.morkato.api.validation.constraints.Mcisidv1Id;
import org.morkato.utility.mcisid.McisidUtil;
import org.morkato.utility.mcisid.ModelType;

import java.util.Objects;

public class Mcisidv1IdValidator implements ConstraintValidator<Mcisidv1Id, String> {
  private ModelType model;
  @Override
  public void initialize(Mcisidv1Id annotation) {
    model = annotation.model();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (Objects.isNull(value))
      return true;
    return McisidUtil.isValidId(value) && McisidUtil.isIdModel(value, model);
  }
}
