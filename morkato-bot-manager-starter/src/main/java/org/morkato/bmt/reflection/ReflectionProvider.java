package org.morkato.bmt.reflection;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.annotation.RegistryExtension;
import org.morkato.bmt.extensions.Extension;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ReflectionProvider {
  public static Set<Class<? extends Extension>> extractAllExtensions(Reflections reflections) {
    return reflections.get(SubTypes.of(Extension.class).add(TypesAnnotated.with(RegistryExtension.class)).asClass())
      .stream()
      .map(clazz -> (Class<? extends Extension>)clazz.asSubclass(Extension.class))
      .collect(Collectors.toSet());
  }

  public static Set<Class<?>> extractAllComponents(Reflections reflections) {
    return reflections.getTypesAnnotatedWith(MorkatoComponent.class);
  }
}
