package org.morkato.http.serv.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Pattern(regexp = "^(https?://)(?:www\\.)?[a-zA-Z0-9\\-\\.]{1,255}(?:/[a-zA-Z0-9\\-\\._~:\\/?#\\[\\]@!$&''()*+,;=]{0,255})?|cdn://[0-9]{15,30}/[^:0-9\s\\/]{2,32}$")
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME) 
@Documented
@Constraint(validatedBy = {})
public @interface BannerSchema {
  String message() default "This banner is invalid!";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
