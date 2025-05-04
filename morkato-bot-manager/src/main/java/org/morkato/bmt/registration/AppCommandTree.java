package org.morkato.bmt.registration;

import org.morkato.bmt.registration.builder.*;
import org.morkato.bmt.registration.payload.SlashCommandPayload;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.components.*;
import java.util.Set;

public interface AppCommandTree {
  <T> TextCommandBuilder<T> text(Command<T> command);
  <T> SlashCommandBuilder<T> slash(Command<T> slashcommand);
  <T> SlashMapperBuilder<T> slashMapper(SlashMapper<T> mapper);
  <T> ObjectParserBuilder<T> objectParser(ObjectParser<T> objectparser);
  <T extends Throwable> CommandExceptionBuilder<T> textExceptionHandler(CommandException<T> exceptionhandler);

  Set<CommandPayload<?>> getPendingCommands();
  Set<SlashCommandPayload<?>> getPendingSlashCommands();
  Set<ObjectParser<?>> getPendingArguments();
  Set<CommandException<?>> getPendingCommandExceptions();
  Set<SlashMapper<?>> getSlashCommandMappers();
}
