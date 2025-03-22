package org.morkato.bmt.annotation;

import org.morkato.bmt.annotation.MorkatoBotManagerToolAnnotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@MorkatoBotManagerToolAnnotation
public @interface RegistryExtension {
}
