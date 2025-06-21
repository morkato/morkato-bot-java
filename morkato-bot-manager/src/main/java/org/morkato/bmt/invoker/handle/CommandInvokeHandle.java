package org.morkato.bmt.invoker.handle;

import org.morkato.bmt.generated.ExceptionsHandleStaticRegistries;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.internal.context.TextCommandContext;
import org.morkato.bmt.components.CommandExceptionHandler;
import org.morkato.bmt.context.CommandContext;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.utility.StringView;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Objects;

public class CommandInvokeHandle<T> implements Runnable {
  protected static final Logger LOGGER = LoggerFactory.getLogger(CommandInvokeHandle.class);
  protected final ExceptionsHandleStaticRegistries exceptions;
  protected final CommandRegistry<T> registry;
  protected final Message message;
  protected final StringView view;
  public CommandInvokeHandle(
    ExceptionsHandleStaticRegistries exceptions,
    CommandRegistry<T> registry,
    Message message,
    StringView view
  ) {
    this.registry = Objects.requireNonNull(registry);
    this.exceptions = Objects.requireNonNull(exceptions);
    this.message = Objects.requireNonNull(message);
    this.view = Objects.requireNonNull(view);
  }

  @Override
  public void run() {
    try {
      final TextCommandContext<T> ctx = registry.spawnContext(message);
      registry.prepareContext(ctx, view);
      this.invokeCommand(ctx);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred.", exc);
    }
  }

  private void invokeCommand(
    TextCommandContext<T> context
  ) {
    try {
      registry.invoke(context);
    } catch (Throwable exc) {
      this.handleException(context, exc);
    }
  }

  @SuppressWarnings("unchecked")
  public <E extends Throwable> void handleException(CommandContext<T> context, E exception) {
    LOGGER.debug("Command: {} ({}) has invoked an unexpected error: {}. Handling...",
      registry.getName(), registry.getCommandClassName(), exception.getClass().getName());
    final CommandExceptionHandler<E> handler = (CommandExceptionHandler<E>)exceptions.getCommandExceptionHandler(exception.getClass());
    if (Objects.nonNull(handler)) {
      handler.doException(context, exception);
      return;
    }
    LOGGER.error("An unexpected error occurred. Not available command exception handler for exception: {}. Show into the display.", exception.getClass().getName(), exception);
    context.reply()
      .setContent("An unexpected error occurred: **" + exception.getClass().getName() + "**. Sorry.")
      .mentionRepliedUser(false)
      .queue();
  }
}
