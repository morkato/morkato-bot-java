package org.morkato.http;

import java.text.MessageFormat;

public class Route {
  public static String normalizePath(String path) {
    String normalized = path.replaceAll("//+", "/");
    if (!normalized.startsWith("/"))
      normalized = "/" + normalized;
    return normalized;
  }
  private final String path;
  public Route(String path) {
    this.path = Route.normalizePath(path);
  }
  public String getRoute(Object... objects) {
    return MessageFormat.format(path, objects);
  }
  public String getRoute() {
    return path;
  }
}
