package org.morkato.boot.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT})
@MorkatoBootAnnotation
@Documented
public @interface NotRequired {
}
