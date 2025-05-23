package org.morkato.api.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Digits(integer = 12, fraction = 0)
@Min(0L)
@Max(1000000000000L)
public @interface MorkatoModelAttribute {
  String message() default "";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
