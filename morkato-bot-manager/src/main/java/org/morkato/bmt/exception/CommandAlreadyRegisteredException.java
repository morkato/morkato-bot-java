package org.morkato.bmt.exception;

import org.morkato.bmt.CommandRegistry;

public class CommandAlreadyRegisteredException extends CommandRegistrationException {
  private final CommandRegistry<?> registry;
  public CommandAlreadyRegisteredException(CommandRegistry<?> registry) {
    super("Command ID: " + registry.getCommandClassName() + " is already registered.");
    this.registry = registry;
  }

  public String getCommandClassName() {
    return registry.getCommandClassName();
  }
}
