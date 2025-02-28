package org.morkato.http.request;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.AbstractHttpEntity;
import org.morkato.http.HTTPClient;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class GetRequestBuilder<ResponseType>  extends RequestBuilderBase<HttpGet, ResponseType> implements RequestBuilder<HttpGet, ResponseType> {
  public GetRequestBuilder(HTTPClient client, Class<ResponseType> clazz) {
    super(client, clazz);
  }

  @Override
  public RequestBuilder<HttpGet, ResponseType> setEntity(AbstractHttpEntity entity) {
    throw new UnsupportedOperationException("GET requests do not support request bodies.");
  }

  @Override
  public CompletableFuture<ResponseType> queue() {
    Objects.requireNonNull(route);
    String path = args == null ? route.getRoute() : route.getRoute(args);
    HttpGet request = new HttpGet(path);
    return this.client.execute(request, parser);
  }
}
