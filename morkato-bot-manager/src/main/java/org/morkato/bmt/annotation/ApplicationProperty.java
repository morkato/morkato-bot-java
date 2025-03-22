package org.morkato.bmt.annotation;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MorkatoBotManagerToolAnnotation
@Documented
public @interface ApplicationProperty {
  @Nonnull String value();
}
