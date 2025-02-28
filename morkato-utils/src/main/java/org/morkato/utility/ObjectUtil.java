package org.morkato.utility;

public class ObjectUtil {
  public static boolean isInstance(Object object, Class<?> clazz) {
    return clazz.isInstance(object);
  }
}
