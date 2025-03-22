package org.morkato.bmt.listener;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class HelloListener extends ListenerAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(HelloListener.class);

  @Override
  public void onReady(ReadyEvent event) {
    /* Carregado quando o bot faz login! Sendo assim, é carregado todas as extensões, errors bouders e comandos! */
    LOGGER.info("Estou conectado, como: {}", event.getJDA().getSelfUser().getName());
  }
}
