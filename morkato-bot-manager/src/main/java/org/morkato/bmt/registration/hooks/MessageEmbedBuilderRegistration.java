package org.morkato.bmt.registration.hooks;

import org.morkato.bmt.registration.registries.MessageEmbedBuilderRegistry;
import org.morkato.bmt.components.MessageEmbedBuilder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class MessageEmbedBuilderRegistration extends SetObjectRegistration<MessageEmbedBuilderRegistry, MessageEmbedBuilder<?>>{
  private final Logger LOGGER = LoggerFactory.getLogger(MessageEmbedBuilderRegistration.class);

  @Override
  public void register(MessageEmbedBuilder<?> registry) {
    this.add(new MessageEmbedBuilderRegistry(registry));
    LOGGER.info("A new embed builder: {} has been registered.", registry.getClass().getName());
  }
}
