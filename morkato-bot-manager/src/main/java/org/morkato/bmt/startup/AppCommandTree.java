package org.morkato.bmt.startup;

import org.morkato.bmt.startup.payload.SlashCommandPayload;
import org.morkato.bmt.startup.payload.CommandPayload;
import org.morkato.bmt.startup.builder.*;
import org.morkato.bmt.components.*;
import java.util.Set;

public interface AppCommandTree {
  <T> TextCommandBuilder<T> text(CommandHandler<T> command);
  <T> SlashCommandBuilder<T> slash(CommandHandler<T> slashcommand);
  <T> SlashMapperBuilder<T> slashMapper(SlashMapper<T> mapper);
  <T> ObjectParserBuilder<T> objectParser(ObjectParser<T> objectparser);
  <T extends Throwable> CommandExceptionBuilder<T> exceptionHandler(CommandExceptionHandler<T> exceptionhandler);

  Set<CommandPayload<?>> getPendingCommands();
  Set<SlashCommandPayload<?>> getPendingSlashCommands();
  Set<ObjectParser<?>> getPendingArguments();
  Set<CommandExceptionHandler<?>> getPendingCommandExceptions();
  Set<SlashMapper<?>> getSlashCommandMappers();
}
