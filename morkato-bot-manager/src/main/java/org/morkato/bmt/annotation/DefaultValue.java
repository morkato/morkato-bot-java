package org.morkato.bmt.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MorkatoBotManagerToolAnnotation
@Documented
public @interface DefaultValue {
  String value();
}
