package org.morkato.boot.annotation;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
@MorkatoBootAnnotation
@Documented
public @interface ApplicationProperty {
  @Nonnull String value();
}
