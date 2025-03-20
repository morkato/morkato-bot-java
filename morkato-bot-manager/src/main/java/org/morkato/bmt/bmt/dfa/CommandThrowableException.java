package org.morkato.bmt.bmt.dfa;

import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bmt.components.CommandException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class CommandThrowableException implements CommandException<Throwable> {
  private static final CommandThrowableException handler = new CommandThrowableException();
  public static CommandThrowableException getHandler() {
    return handler;
  }
  Logger logger = LoggerFactory.getLogger(CommandThrowableException.class);
  @Override
  public void doException(TextCommandContext<?> context, Throwable exception){
    logger.warn("Command ID: {} has invoked a error: {}. Ignoring.", context.getCommand().getClass().getName(), exception.getClass());
    exception.printStackTrace();
  }
}
