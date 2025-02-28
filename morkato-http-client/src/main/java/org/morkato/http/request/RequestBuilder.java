package org.morkato.http.request;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.morkato.http.Route;
import java.util.concurrent.CompletableFuture;

public interface RequestBuilder<T extends HttpRequestBase, ResponseType> {
  RequestBuilder<T, ResponseType> setRoute(Route route);
  RequestBuilder<T, ResponseType> setArgs(Object... args);
  RequestBuilder<T, ResponseType> setEntity(AbstractHttpEntity entity);
  RequestBuilder<T, ResponseType> setHeader(Header header);
  RequestBuilder<T, ResponseType> setHeader(String key, String value);
  CompletableFuture<ResponseType> queue();
}
