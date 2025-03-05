package org.morkato.bmt.errors;

public class CommandArgumentParserNotFound extends CommandRegistrationException {
  private final Class<?> parserClazz;
  public CommandArgumentParserNotFound(Class<?> clazz) {
    super("Command argument parser for " + clazz.getName() + " is not found.");
    this.parserClazz = clazz;
  }

  public Object getParserClassName() {
    return parserClazz.getName();
  }
}
