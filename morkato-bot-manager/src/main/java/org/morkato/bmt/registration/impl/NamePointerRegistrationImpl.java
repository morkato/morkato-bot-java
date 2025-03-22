package org.morkato.bmt.registration.impl;

import org.morkato.bmt.CommandRegistry;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.hooks.NamePointer;
import org.morkato.bmt.registration.NamePointerRegistration;
import org.morkato.bmt.registration.TextCommandRegistration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map;

public class NamePointerRegistrationImpl implements NamePointerRegistration {
  private static final Logger LOGGER = LoggerFactory.getLogger(NamePointerRegistration.class);
  private final Map<String, CommandRegistry<?>> pointers = new HashMap<>();
  private final TextCommandRegistration registries;

  public NamePointerRegistrationImpl(TextCommandRegistration registries) {
    Objects.requireNonNull(registries);
    this.registries = registries;
  }

  @Override
  public CommandRegistry<?> getCommandRegistry(String pointer) {
    return pointers.get(pointer);
  }

  @Override
  public void register(NamePointer registry) {
    String pointer = registry.getName();
    Class<? extends Command<?>> command = registry.getCommand();
    CommandRegistry<?> commandRegistry = registries.getRegistry(command);
    if (Objects.isNull(commandRegistry)) {
      LOGGER.warn("Command ID: {} is not registered. Ignoring registry for name: {}.", command.getName(), pointer);
      return;
    }
    pointers.put(pointer, commandRegistry);
    LOGGER.info("Name pointer: {} has been registered for command: {}.", pointer, command.getName());
  }

  @Override
  public void clear() {
    pointers.clear();
  }
}
