package org.morkato.bmt;

import org.morkato.bmt.registration.TextCommandExceptionRegistration;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.impl.TextCommandContextImpl;
import org.morkato.utility.StringView;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandRegistry<T> {
  private final Logger LOGGER = LoggerFactory.getLogger(CommandRegistry.class);
  private final Map<String, CommandRegistry<?>> subcommands = new HashMap<>();
  private final TextCommandExceptionRegistration exceptions;
  private final Command<T> command;
  private final ObjectParser<T> parser;
  private final Class<T> args;
  @SuppressWarnings("unchecked")
  public CommandRegistry(TextCommandExceptionRegistration exceptions, Command<T> command, ObjectParser<T> parser) {
    Class<T> clazz = (Class<T>)Command.getArgument(command.getClass());
    this.exceptions = exceptions;
    this.command = command;
    this.parser = parser;
    this.args = clazz;
  }
  @Nonnull
  @SuppressWarnings("unchecked")
  public Class<? extends Command<T>> getCommandClass() {
    return (Class<? extends Command<T>>)this.command.getClass();
  }
  @Nonnull
  public String getCommandClassName() {
    return this.getCommandClass().getName();
  }
  @Nonnull
  public Class<T> getArgumentClass() {
    return this.args;
  }
  @Nonnull
  public String getArgumentClassName() {
    return this.getArgumentClass().getName();
  }
  public Runnable prepareRunnable(Message message, StringView view) {
    return () -> this.invoke(message, view);
  }

  public boolean hasSubCommands() {
    return !this.subcommands.isEmpty();
  }

  public CommandRegistry<?> getSubCommand(String pointer) {
    return this.subcommands.get(pointer);
  }

  public void registerSubCommand(String pointer, CommandRegistry<?> children) {
    this.subcommands.put(pointer, children);
  }

  public void invoke(Message message, StringView view) {
    TextCommandContextImpl<T> context = new TextCommandContextImpl<>(command, message, null);
    try {
      view.skipWhitespace();
      if (args != NoArgs.class && !view.eof()) {
        view.skipWhitespace();
        T argument = parser.parse(context, view.rest());
        context.setArgs(argument);
      }
      command.invoke(context);
    } catch (Throwable exc) {
      this.onError(context, exc);
    }
  }
  @SuppressWarnings("unchecked")
  public <E extends Throwable> void onError(TextCommandContext<?> context, E exc) {
    CommandException<E> handler = (CommandException<E>)this.exceptions.get(exc.getClass());
    if (Objects.isNull(handler)) {
      LOGGER.error("Command ID: {} has invoked a error: {}. Ignoring.", context.getCommand().getClass().getName(), exc.getClass(), exc);
      return;
    }
    handler.doException(context, exc);
  }

  public Class<? extends Command<?>> getParentClass() {
    if (Objects.isNull(command.parent()))
      return null;
    return command.parent().parent();
  }

  public String getChildrenName() {
    if (Objects.isNull(command.parent()))
      return null;
    return command.parent().name();
  }
}
