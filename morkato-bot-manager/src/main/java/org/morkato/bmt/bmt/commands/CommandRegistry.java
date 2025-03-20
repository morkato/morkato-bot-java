package org.morkato.bmt.bmt.commands;

import org.morkato.bmt.bmt.argument.NoArgs;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.components.CommandException;
import org.morkato.bmt.bmt.components.ObjectParser;
import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bmt.dfa.CommandThrowableException;
import org.morkato.bmt.bmt.impl.TextCommandContextImpl;
import org.morkato.bmt.bmt.registration.TextCommandExceptionRegistration;
import org.morkato.bmt.utility.StringView;
import net.dv8tion.jda.api.entities.Message;
import javax.annotation.Nonnull;

public class CommandRegistry<T> {
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
  public void invoke(Message message, StringView view) {
    TextCommandContextImpl<T> context = new TextCommandContextImpl<>(command, message, null);
    try {
      if (args != NoArgs.class) {
        view.skipWhitespace();
        T argument = (T)parser.parse(context, view.rest());
        context.setArgs(argument);
      }
      command.invoke(context);
    } catch (Throwable exc) {
      this.onError(context, exc);
    }
  }
  @SuppressWarnings("unchecked")
  public <E extends Throwable> void onError(TextCommandContext<?> context, E exc) {
    CommandException<E> handler = (CommandException<E>) this.exceptions.get(exc.getClass());
    if (handler == null)
      handler = (CommandException<E>)CommandThrowableException.getHandler();
    handler.doException(context, exc);
  }
}
