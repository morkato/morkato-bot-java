package org.morkato.bmt.api.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^\\d+$")
public @interface NumberOnly {
  String message() default "Apenas números são permitidos";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
