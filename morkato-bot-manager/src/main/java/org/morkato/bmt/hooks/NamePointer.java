package org.morkato.bmt.hooks;

import org.morkato.bmt.CommandRegistry;
import org.morkato.bmt.components.Command;

public class NamePointer {
  private final String name;
  private final Class<? extends Command<?>> command;

  public NamePointer(String name, Class<? extends Command<?>> command) {
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
