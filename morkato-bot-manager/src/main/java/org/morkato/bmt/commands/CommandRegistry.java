package org.morkato.bmt.commands;

import org.morkato.bmt.argument.ArgumentParser;
import org.morkato.bmt.argument.NoArgs;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.Extension;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.dfa.CommandThrowableException;
import org.morkato.bmt.impl.ContextImpl;
import org.morkato.bmt.management.CommandExceptionManager;
import org.morkato.utility.StringView;
import net.dv8tion.jda.api.entities.Message;
import javax.annotation.Nonnull;

public class CommandRegistry<T> {
  private final CommandExceptionManager exceptions;
  private final Command<T> command;
  private final Class<? extends Extension> extension;
  private final Class<T> args;
  public CommandRegistry(CommandExceptionManager exceptions, Command<T> command) {
    Class<T> clazz = (Class<T>) Command.getArgument(command.getClass());
    Class<? extends Extension> extension = Command.getExtension(command.getClass());
    this.exceptions = exceptions;
    this.extension = extension;
    this.command = command;
    this.args = clazz;
  }
  public Class<? extends Extension> getOwnerExtension() {
    return this.extension;
  }
  @Nonnull
  public Class<? extends Command> getCommandClass() {
    return this.command.getClass();
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
  public Runnable prepareRunnable(Message message, ArgumentParser parser, StringView view) {
    return () -> this.invoke(message, parser, view);
  }
  public void invoke(Message message, ArgumentParser parser, StringView view) {
    ContextImpl<T> context = new ContextImpl<>(command, message, null);
    try {
      if (args != NoArgs.class)
        context.setArgs(parser.parse(args, context, view));
      command.invoke(context);
    } catch (Throwable exc) {
      this.onError(context, exc);
    }
  }
  @SuppressWarnings("unchecked")
  public <E extends Throwable> void onError(TextCommandContext<?> context, E exc) {
    CommandException<E> handler = (CommandException<E>) this.exceptions.get(exc.getClass());
    if (handler == null)
      handler = (CommandException<E>) CommandThrowableException.getHandler();
    handler.doException(context, exc);
  }
}
