package org.morkato.bmt.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@MorkatoBotManagerToolAnnotation
@Documented
public @interface DecoratorFactory {
}
