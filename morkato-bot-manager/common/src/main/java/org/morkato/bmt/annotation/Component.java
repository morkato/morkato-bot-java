package org.morkato.bmt.annotation;

import org.morkato.bmt.extensions.Extension;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@MorkatoBotManagerToolAnnotation
@Documented
public @interface Component{
  Class<? extends Extension> extension() default Extension.class;
}
