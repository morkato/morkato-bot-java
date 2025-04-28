package org.morkato.boot.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@MorkatoBootAnnotation
@Documented
public @interface AutoInject {
}
