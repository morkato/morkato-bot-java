package com.morkato.bmt.errors;

import com.morkato.bmt.commands.Command;
import javax.annotation.Nonnull;

public class CommandAlreadyRegistered extends MorkatoBotException {
  private Command command;
  public CommandAlreadyRegistered(
    @Nonnull Command command
  ) {
    super("Command ID: " + command.getClass().getName() + " Is already registered in CommandManager.");
    this.command = command;
  }
  public Command getCommand() {
    return this.command;
  }
}
