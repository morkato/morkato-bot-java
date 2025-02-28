package org.morkato.bmt.annotation;

import org.morkato.bmt.components.Extension;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Deprecated
public @interface ExtensionRegistry{
  @Nonnull
  Class<? extends Extension> extension();
}
