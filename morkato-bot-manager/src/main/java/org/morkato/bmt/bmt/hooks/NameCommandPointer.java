package org.morkato.bmt.bmt.hooks;

import org.morkato.bmt.bmt.components.Command;
import java.util.Objects;

public class NameCommandPointer {
  private final String name;
  private final Class<? extends Command<?>> command;

  public NameCommandPointer(String name, Class<? extends Command<?>> command) {
    Objects.requireNonNull(command);
    Objects.requireNonNull(name);
    this.name = name;
    this.command = command;
  }

  public String getName() {
    return name;
  }

  public Class<? extends Command<?>> getCommand() {
    return command;
  }
}
