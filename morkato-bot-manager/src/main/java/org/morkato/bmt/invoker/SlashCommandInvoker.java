package org.morkato.bmt.invoker;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.morkato.bmt.BotCore;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;
import org.morkato.bmt.commands.SlashCommandContext;
import org.morkato.bmt.components.CommandExceptionHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Objects;


public class SlashCommandInvoker implements Invoker<SlashCommandInteractionEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SlashCommandInvoker.class);
  private boolean ready = false;
  private BotCore core;

  public void start(BotCore core) {
    if (ready)
      return;
    this.core = Objects.requireNonNull(core);
    this.ready = true;
  }

  @Override
  public void shutdown() {
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  @Override
  public void invoke(SlashCommandInteractionEvent event) {
    if (!isReady()) {
      LOGGER.debug("SlashCommandInvoker is invoked, but, is not ready to execute slashcommands. Ignoring.");
      return;
    }
    this.ambientInvoke(event);
  }

  @SuppressWarnings("unchecked")
  private <T> void ambientInvoke(SlashCommandInteractionEvent event) {
    try {
      final SlashCommandRegistry<T> registry = (SlashCommandRegistry<T>)core.getSlashCommand(event.getName());
      final SlashCommandContext<T> ctx = registry.bindContext(core, event);
      this.invokeRegistry(registry, ctx);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred.", exc);
    }
  }

  private <T> void invokeRegistry(
    SlashCommandRegistry<T> registry,
    SlashCommandContext<T> context
  ) {
    try {
      registry.invoke(context);
    } catch (Exception exc) {
      this.handleException(registry, context, exc);
    }
  }

  @SuppressWarnings("unchecked")
  private <T, E extends Exception> void handleException(
    SlashCommandRegistry<T> registry,
    SlashCommandContext<T> context,
    E exception
  ) {
    LOGGER.debug("Command: {} ({}) has invoked an unexpected error: {}. Handling...",
      registry.getName(), registry.getCommandClassName(), exception.getClass().getName());
    final CommandExceptionHandler<E> handler = (CommandExceptionHandler<E>)core.getCommandExceptionHandler(exception.getClass());
    if (Objects.nonNull(handler)) {
      handler.doException(context, exception);
      return;
    }
    LOGGER.error("An unexpected error occurred. Not available command exception handler for exception: {}. Show into the display.", exception.getClass().getName(), exception);
    context.respond()
      .setContent("An unexpected error occurred: **" + exception.getClass().getName() + "**. Sorry.")
      .queue();
  }
}
