package org.morkato.http.repository;

import org.morkato.api.repository.SimpleRepositoryCentral;
import org.morkato.http.HTTPClient;

public class HTTPRepositoryCentral extends SimpleRepositoryCentral {
  private final HTTPClient client;
  public static HTTPRepositoryCentral create(HTTPClient client) {
    return new HTTPRepositoryCentral(client);
  }
  protected HTTPRepositoryCentral(HTTPClient client) {
    super(new HTTPArtRepository(client));
    this.client = client;
  }
}
