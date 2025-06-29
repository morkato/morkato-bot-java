package org.morkato.bmt.commands;

import org.morkato.bmt.BotCore;
import org.morkato.bmt.generated.registries.CommandExceptionRegistry;
import org.morkato.bmt.generated.registries.CommandRegistry;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.utility.StringView;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Objects;

public class CommandInvokeHandle<T> implements Runnable {
  protected static final Logger LOGGER = LoggerFactory.getLogger(CommandInvokeHandle.class);
  protected final BotCore core;
  protected final CommandRegistry<T> registry;
  protected final Message message;
  protected final String invokedCommandName;
  protected final StringView view;
  public CommandInvokeHandle(
    BotCore core,
    CommandRegistry<T> registry,
    Message message,
    String invokedCommandName,
    StringView view
  ) {
    this.core = Objects.requireNonNull(core);
    this.registry = Objects.requireNonNull(registry);
    this.message = Objects.requireNonNull(message);
    this.invokedCommandName = Objects.requireNonNull(invokedCommandName);
    this.view = Objects.requireNonNull(view);
  }

  @Override
  public void run() {
    try {
      final TextCommandContext<T> ctx = new TextCommandContext<>(
        core, registry, message, invokedCommandName);
      this.invokeCommand(ctx);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred.", exc);
    }
  }

  private void invokeCommand(TextCommandContext<T> context) {
    try {
      registry.prepareContext(context, view);
      registry.invoke(context);
    } catch (Exception exc) {
      this.handleException(context, exc);
    }
  }

  @SuppressWarnings("unchecked")
  public <E extends Exception> void handleException(CommandContext<T> context, E exception) {
    LOGGER.debug("Command: {} ({}) has invoked an unexpected error: {}. Handling...",
      registry.getName(), registry.getCommandClassName(), exception.getClass().getName());
    final CommandExceptionRegistry<E> handler = (CommandExceptionRegistry<E>)core.getCommandExceptionHandler(exception.getClass());
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
