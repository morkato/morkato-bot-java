package org.morkato.bmt.generated;

import org.morkato.bmt.Shutdownable;

public class ApplicationStaticRegistries implements Shutdownable {
  private final ParsersStaticRegistries parsers;
  private final ExceptionsHandleStaticRegistries exceptions;
  private final CommandsStaticRegistries commands;

  public ApplicationStaticRegistries(
    ParsersStaticRegistries parsers,
    ExceptionsHandleStaticRegistries exceptions,
    CommandsStaticRegistries commands
  ) {
    this.parsers = parsers;
    this.exceptions = exceptions;
    this.commands = commands;
  }

  public ParsersStaticRegistries getParsers() {
    return parsers;
  }

  public ExceptionsHandleStaticRegistries getExceptions() {
    return exceptions;
  }

  public CommandsStaticRegistries getCommands() {
    return commands;
  }

  public void clear() {
  }

  @Override
  public void shutdown() {
    this.clear();
  }
}
