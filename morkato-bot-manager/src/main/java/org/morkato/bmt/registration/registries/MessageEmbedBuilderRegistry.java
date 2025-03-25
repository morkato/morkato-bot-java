package org.morkato.bmt.registration.registries;

import org.morkato.bmt.components.MessageEmbedBuilder;

public class MessageEmbedBuilderRegistry implements Registry<MessageEmbedBuilder<?>> {
  private final MessageEmbedBuilder<?> builder;

  public MessageEmbedBuilderRegistry(MessageEmbedBuilder<?> builder) {
    this.builder = builder;
  }

  @Override
  public MessageEmbedBuilder<?> getRegistry() {
    return builder;
  }
}
