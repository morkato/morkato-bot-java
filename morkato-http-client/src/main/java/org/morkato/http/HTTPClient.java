package org.morkato.http;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.morkato.http.request.GetRequestBuilder;
import org.morkato.http.request.PostRequestBuilder;
import org.morkato.http.request.RequestBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.*;

public class HTTPClient {
  private final CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
  private final HttpHost hostname;
  private boolean isOpened = false;
  public HTTPClient(URI uri) {
    String scheme = uri.getScheme() != null ? uri.getScheme() : "http";
    String host = uri.getHost();
    int port = uri.getPort();
    port = (port == -1) ? (scheme.equals("http") ? 80 : 443) : port;
    this.hostname = new HttpHost(host, port, scheme);
  }
  public boolean isOpen() {
    return this.isOpened;
  }
  public synchronized void start() {
    if (this.isOpened)
      return;
    this.client.start();
    this.isOpened = true;
  }
  public <T> CompletableFuture<T> execute(HttpRequestBase base, Class<T> parser) {
    CompletableFuture<T> response = new CompletableFuture<>();
    this.client.execute(hostname, base, new RequestExecutor<>(response, parser));
    return response;
  }
  public <T> RequestBuilder<HttpGet, T> get(Class<T> parser) {
    return new GetRequestBuilder<>(this, parser);
  }
  public <T> RequestBuilder<HttpPost, T> post(Class<T> parser) {
    return new PostRequestBuilder<>(this, parser);
  }
  public synchronized void shutdown() {
    try {
      this.client.close();
      this.isOpened = false;
    } catch (IOException exc) {}
  }

}
