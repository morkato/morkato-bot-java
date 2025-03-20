package org.morkato.bmt.bmt.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MorkatoBotManagerToolAnnotation
@Documented
public @interface AutoInject {
}
