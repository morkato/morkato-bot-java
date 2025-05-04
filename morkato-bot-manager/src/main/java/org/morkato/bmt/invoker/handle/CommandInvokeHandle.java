package org.morkato.bmt.invoker.handle;

import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.context.CommandContext;
import org.morkato.bmt.internal.context.TextCommandContext;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.utility.StringView;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Objects;

public class CommandInvokeHandle<T> implements Runnable {
  protected static final Logger LOGGER = LoggerFactory.getLogger(CommandInvokeHandle.class);
  protected final MapRegistryManagement<Class<? extends Throwable>, CommandException<?>> exceptions;
  protected final CommandRegistry<T> registry;
  protected final Message message;
  protected final StringView view;
  public CommandInvokeHandle(
    MapRegistryManagement<Class<? extends Throwable>, CommandException<?>> exceptions,
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
    final TextCommandContext<T> ctx = registry.spawnContext(message);
    try {
      registry.prepareContext(ctx, view);
      registry.invoke(ctx);
    } catch (Throwable exc) {
      this.onException(ctx, exc);
    }
  }

  @SuppressWarnings("unchecked")
  public <E extends Throwable> void onException(CommandContext<T> context, E exception) {
    CommandException<E> handler = (CommandException<E>)exceptions.get(exception.getClass());
    if (Objects.isNull(handler)) {
      LOGGER.error("Command ID: {} has invoked a error: {}. Ignoring.", context.getCommand().getClass().getName(), exceptions.getClass(), exception);
      return;
    }
    handler.doException(context, exception);
  }
}
