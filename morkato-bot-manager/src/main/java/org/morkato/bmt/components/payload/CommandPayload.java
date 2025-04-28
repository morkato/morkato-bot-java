package org.morkato.bmt.components.payload;

import org.morkato.bmt.components.Command;
import java.util.Objects;

public class CommandPayload<T> {
  private final Command<T> command;
  private final String[] aliases;
  private final String description;
  private final Class<T> argumentClazz;

  @SuppressWarnings("unchecked")
  public CommandPayload(
    Command<T> command,
    String[] aliases,
    String description
  ) {
    this.command = Objects.requireNonNull(command);
    this.aliases = Objects.requireNonNull(aliases);
    this.description = description;
    this.argumentClazz = (Class<T>)Command.getArgument(command.getClass());
  }

  public Command<T> getCommand() {
    return command;
  }

  public String[] getAliases() {
    return aliases;
  }

  public String getDescription() {
    return description;
  }

  public Class<T> getArgumentClass() {
    return argumentClazz;
  }
}
