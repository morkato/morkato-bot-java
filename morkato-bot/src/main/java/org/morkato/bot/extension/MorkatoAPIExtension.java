package org.morkato.bot.extension;

import org.morkato.bmt.ApplicationContextBuilder;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.components.BaseExtension;
import org.morkato.bmt.context.ExtensionSetupContext;
import org.morkato.api.repository.RepositoryCentral;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@MorkatoComponent
public class MorkatoAPIExtension extends BaseExtension {
  private static final Logger LOGGER = LoggerFactory.getLogger(MorkatoAPIExtension.class);
  @AutoInject
  public RepositoryCentral repository;
//  private HTTPClient client;
  @Override
  public void start(ApplicationContextBuilder app) throws Throwable {
//    String hostname = app.getProperty("morkato.client.http.url", "http://localhost:5500");
//    LOGGER.info("Configuring all traffic for host: {} in morkato api.", hostname);
//    URI uri = new URI(hostname);
//    client = new HTTPClient(uri);
//    app.inject(HTTPRepositoryCentral.create(client));
//    client.start();
  }

  @Override
  public void setup(ExtensionSetupContext ctx) throws Throwable{
  }

  @Override
  public void close() {
//    if (client == null)
//      return;
//    client.shutdown();
//    client = null;
  }
}
