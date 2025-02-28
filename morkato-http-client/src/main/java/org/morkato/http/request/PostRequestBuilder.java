package org.morkato.http.request;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.morkato.http.HTTPClient;
import org.morkato.http.RequestExecutor;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class PostRequestBuilder<ResponseType> extends RequestBuilderBase<HttpPost, ResponseType> implements RequestBuilder<HttpPost, ResponseType> {
  private AbstractHttpEntity entity = null;
  public PostRequestBuilder(HTTPClient client, Class<ResponseType> parser) {
    super(client, parser);
  }
  @Override
  public RequestBuilder<HttpPost, ResponseType> setEntity(AbstractHttpEntity entity) {
    this.entity = entity;
    return this;
  }

  @Override
  public CompletableFuture<ResponseType> queue() {
    Objects.requireNonNull(route);
    String path = this.args == null ? route.getRoute() : route.getRoute(args);
    HttpPost request = new HttpPost(path);
    if (entity != null)
      request.setEntity(entity);
    return this.client.execute(request, parser);
  }
}
