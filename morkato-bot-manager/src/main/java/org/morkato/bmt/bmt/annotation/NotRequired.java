package org.morkato.bmt.bmt.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT})
@MorkatoBotManagerToolAnnotation
@Documented
public @interface NotRequired {
}
