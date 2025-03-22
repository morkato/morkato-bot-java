package org.morkato.api.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Size(min = 2, max = 96)
@Pattern(regexp ="^(https?://)(?:www\\.)?[a-zA-Z0-9\\-.]{1,255}(?:/[a-zA-Z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=]{0,255})?$")
public @interface MorkatoModelBanner {
  String message() default "O banner informado é invalido.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
