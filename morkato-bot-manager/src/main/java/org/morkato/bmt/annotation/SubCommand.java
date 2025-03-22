package org.morkato.bmt.annotation;

import org.morkato.bmt.components.Command;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@MorkatoBotManagerToolAnnotation
@Documented
public @interface SubCommand {
  String value();
  Class<? extends Command<?>> parent();
}
