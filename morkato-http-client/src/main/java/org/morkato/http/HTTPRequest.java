package org.morkato.http;

import org.apache.http.client.methods.HttpRequestBase;
import java.util.Objects;
import java.net.URI;

public class HTTPRequest extends HttpRequestBase {
  private final RequestMethod method;
  public static HTTPRequest from(RequestMethod method, Route route, Object... args) {
    return new HTTPRequest(method, route.getRoute(args));
  }
  protected HTTPRequest(RequestMethod method, String path) {
    this.method = method;
    Objects.nonNull(method);
    this.setURI(URI.create(path));
  }
  @Override
  public String getMethod() {
    return this.method.getRaValue();
  }
}
