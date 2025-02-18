package com.morkato.utility;

import com.google.common.reflect.ClassPath;
import java.lang.annotation.Annotation;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

public class ClassUtility {
  @Nonnull
  public static Set<ClassPath.ClassInfo> getAllClassFromPackage(
    @Nonnull String packageName
  ) throws IOException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    ClassPath path = ClassPath.from(classLoader);
    return path.getTopLevelClassesRecursive(packageName);
  }
  @Nonnull
  public static <T extends Annotation> Map<Class<?>, T> getAllClassFromPackageAndAnnotation(
    @Nonnull String packageName,
    @Nonnull Class<T> tClass
  ) throws IOException {
    Map<Class<?>, T> classMap = new HashMap<>();
    Set<ClassPath.ClassInfo> classNames = getAllClassFromPackage(packageName);
    for (ClassPath.ClassInfo classInfo : classNames) {
      Class<?> clazz = classInfo.load();
      if (!clazz.isAnnotationPresent(tClass))
        continue;
      T value = clazz.getAnnotation(tClass);
      classMap.put(clazz, value);
    }
    return classMap;
  }
  public static boolean isSubclass(@Nonnull Class<?> clazz, Class<?> another) {
    if (clazz.equals(another)) {
      return true;
    }
    Class<?> superClazz = clazz.getSuperclass();
    if (superClazz == null) {
      return false;
    }
    return isSubclass(superClazz, another);
  }
}
