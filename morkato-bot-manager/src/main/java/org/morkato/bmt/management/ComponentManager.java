package org.morkato.bmt.management;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.utility.ClassUtility;
import org.morkato.bmt.components.Extension;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentManager<T> {
  @Nonnull
  public static ComponentManager<Extension> from(
    Package pack
  ) throws IOException {
    Map<Class<?>, MorkatoComponent> rawComponents = ClassUtility.getAllClassFromPackageAndAnnotation(pack.getName(), MorkatoComponent.class);
    ComponentManager<Extension> components = new ComponentManager<>(Extension.class);
    for (Map.Entry<Class<?>, MorkatoComponent> entry : rawComponents.entrySet()) {
      MorkatoComponent component = entry.getValue();
      Class<?> clazz = entry.getKey();
      components.put(component.extension(), clazz);
    }
    return components;
  }
  @Nonnull
  private final Map<Class<?>, Class<? extends T>> components = new HashMap<>();
  @Nonnull
  private final Class<T> clazz;
  protected ComponentManager(Class<T> clazz) {
    this.clazz = clazz;
  }
  public Set<Class<?>> from(Class<? extends T> clazz) {
    return this.components
      .entrySet()
      .stream()
      .filter((it) -> it.getValue().equals(clazz) && !Extension.class.isAssignableFrom(it.getKey()))
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }
  @SuppressWarnings("unchecked")
  public Set<Class<?>> from(T object) {
    return this.from((Class<? extends T>) object.getClass());
  }
  public void put(Class<? extends T> clazz, Class<?> component) {
    this.components.put(component, clazz);
  }
  public Set<Class<? extends T>> getEntries() {
    return this.components.keySet().stream().filter(clazz::isAssignableFrom).map((it) -> (Class<? extends T>)it).collect(Collectors.toSet());
  }
}
