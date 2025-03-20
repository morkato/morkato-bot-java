package org.morkato.bmt.annotation;

import org.morkato.bmt.bmt.annotation.MorkatoBotManagerToolAnnotation;
import org.morkato.bmt.bmt.extensions.Extension;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@MorkatoBotManagerToolAnnotation
@Documented
public @interface MorkatoComponent {
  Class<? extends Extension> extension() default Extension.class;
}
