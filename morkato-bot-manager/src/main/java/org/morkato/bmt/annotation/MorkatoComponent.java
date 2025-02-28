package org.morkato.bmt.annotation;

import org.morkato.bmt.components.Extension;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MorkatoComponent {
  Class<? extends Extension> extension() default Extension.class;
}
