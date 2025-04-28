package org.morkato.bmt.hooks;

import org.morkato.bmt.components.Command;

import java.util.Objects;

public class NamePointer {
  private final String name;
  private final Class<? extends Command<?>> command;

  public NamePointer(String name, Class<? extends Command<?>> command) {
    this.name = Objects.requireNonNull(name);
    this.command = Objects.requireNonNull(command);
  }

  public String getName() {
    return name;
  }

  public Class<? extends Command<?>> getCommand() {
    return command;
  }
}
