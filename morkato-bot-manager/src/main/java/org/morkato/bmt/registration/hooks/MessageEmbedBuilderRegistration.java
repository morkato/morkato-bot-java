package org.morkato.bmt.registration.hooks;

import org.morkato.bmt.components.MessageEmbedBuilder;
import org.morkato.bmt.registration.RegisterManagement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MessageEmbedBuilderRegistration implements RegisterManagement<MessageEmbedBuilder<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageEmbedBuilderRegistration.class);
  private final Set<MessageEmbedBuilder<?>> registries = new HashSet<>();

  @Override
  public void register(MessageEmbedBuilder<?> registry) {
    registries.add(registry);
    LOGGER.info("A new embed builder: {} has been registered.", registry.getClass().getName());
  }

  @Override
  public void clear() {
    registries.clear();
  }

  @Override
  public int size() {
    return registries.size();
  }

  @Override
  @Nonnull
  public Iterator<MessageEmbedBuilder<?>> iterator() {
    return registries.iterator();
  }
}
