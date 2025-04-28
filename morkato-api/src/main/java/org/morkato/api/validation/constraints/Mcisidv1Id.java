package org.morkato.api.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.morkato.api.entity.MorkatoModelType;
import org.morkato.api.validation.validator.Mcisidv1IdValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = Mcisidv1IdValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mcisidv1Id {
  String message() default "";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  MorkatoModelType model() default MorkatoModelType.GENERIC;
}
