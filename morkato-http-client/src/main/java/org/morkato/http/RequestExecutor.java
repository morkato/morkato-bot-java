package org.morkato.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.morkato.utility.JacksonObjectMapper;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class RequestExecutor<T> implements FutureCallback<HttpResponse> {
  public static final ObjectMapper mapper = new ObjectMapper();
  private final CompletableFuture<T> future;
  private final Class<T> parser;
  public RequestExecutor(CompletableFuture<T> future, Class<T> parser) {
    this.future = future;
    this.parser = parser;
  }
  public static Charset getCharset(HttpResponse response) {
    // TODO: Add support for charset
    return StandardCharsets.UTF_8;
  }
  @Override
  @SuppressWarnings("unchecked")
  public void completed(HttpResponse response) {
    try {
      if (parser.equals(HttpResponse.class)) future.complete((T)response);
      HttpEntity entity = response.getEntity();
      if (parser.equals(HttpEntity.class)) future.complete((T)entity);
      String type = entity.getContentType().getValue();
      if (parser.equals(String.class)) {
        byte[] content = entity.getContent().readAllBytes();
        future.complete((T)new String(content, getCharset(response)));
        return;
      }
      if (type.startsWith("application/json")) {
        byte[] content = entity.getContent().readAllBytes();
        future.complete(JacksonObjectMapper.fromJson(new String(content, getCharset(response)), parser));
        return;
      }
      future.completeExceptionally(new RuntimeException());
    } catch (Exception exc) {
      this.future.completeExceptionally(exc);
    }
  }
  @Override
  public void failed(Exception exception) {
    this.future.completeExceptionally(exception);
  }
  @Override
  public void cancelled() {
    this.future.cancel(true);
  }
}
