package org.morkato.http;

import org.morkato.api.art.Art;
import org.morkato.http.repository.HTTPArtRepository;
import org.morkato.api.repository.ArtRepository;

import java.net.URI;
import java.util.Arrays;

public class  Main {
  public static void main(String[] args) throws Exception {
    final HTTPClient client = new HTTPClient(new URI("http://localhost:5500"));
    ArtRepository repo = new HTTPArtRepository(client);
    try {
      client.start();
      Art[] arts = repo.fetchAll("971803172056219728").get();
      System.out.println(Arrays.toString(arts));
//      Art art = repo.create(new ArtCreateQuery("971803172056219728", "Nome Teste",ArtType.RESPIRATION, null, null)).get();
//      System.out.println(art);
    } catch (Exception exc) {
      exc.printStackTrace();
    } finally{
      client.shutdown();
    }
  }
}