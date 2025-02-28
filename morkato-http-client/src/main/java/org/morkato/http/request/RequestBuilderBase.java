package org.morkato.http.request;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;
import org.morkato.http.HTTPClient;
import org.morkato.http.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class RequestBuilderBase<RequestType extends HttpRequestBase, ResponseType> implements RequestBuilder<RequestType, ResponseType> {
  protected final List<Header> headers = new ArrayList<>();
  protected final Class<ResponseType> parser;
  protected final HTTPClient client;
  protected Object[] args = null;
  protected Route route = null;
  public RequestBuilderBase(HTTPClient client, Class<ResponseType> clazz) {
    this.client = client;
    this.parser = clazz;
  }

  @Override
  public abstract RequestBuilder setEntity(AbstractHttpEntity entity);
  @Override
  public abstract CompletableFuture<ResponseType> queue();

  @Override
  public RequestBuilder<RequestType, ResponseType> setRoute(Route route) {
    Objects.requireNonNull(route);
    this.route = route;
    return this;
  }

  @Override
  public RequestBuilder<RequestType, ResponseType> setArgs(Object... args) {
    Objects.requireNonNull(args);
    this.args = args;
    return this;
  }

  @Override
  public RequestBuilder<RequestType, ResponseType> setHeader(Header header) {
    headers.add(header);
    return this;
  }
  @Override
  public RequestBuilder<RequestType, ResponseType> setHeader(String key, String value) {
    return this.setHeader(new BasicHeader(key, value));
  }
}
