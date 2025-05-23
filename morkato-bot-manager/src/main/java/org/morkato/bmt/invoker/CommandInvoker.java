package org.morkato.bmt.invoker;

import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.invoker.handle.CommandInvokeHandle;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.context.invoker.CommandInvokerContext;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.utility.StringView;
import net.dv8tion.jda.api.entities.Message;
import java.util.concurrent.*;
import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class CommandInvoker implements Invoker<CommandInvokerContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandInvoker.class);
  private static final int MAX_THREAD_POOL_SIZE = 10;
  private MapRegistryManagement<String, CommandRegistry<?>> commands;
  private MapRegistryManagement<Class<? extends Throwable>, CommandException<?>> exceptions;
  private ExecutorService service = null;
  private boolean ready = false;

  public synchronized void start(
    MapRegistryManagement<String, CommandRegistry<?>> commands,
    MapRegistryManagement<Class<? extends Throwable>, CommandException<?>> exceptions
  ) {
    if (ready)
      return;
    Objects.requireNonNull(commands);
    Objects.requireNonNull(exceptions);
    this.service = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE, runnable -> {
      Thread thread = new Thread(runnable);
      thread.setName("morkato-command-invoker");
      return thread;
    });
    this.commands = commands;
    this.exceptions = exceptions;
    this.ready = true;
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  public CompletableFuture<Void> runAsync(Runnable runnable) {
    LOGGER.trace("Run in async runnable: {}", runnable);
    return CompletableFuture.runAsync(runnable, service);
  }

  public CommandRegistry<?> spawn(StringView view) {
    final String commandname = view.word();
    if (Objects.isNull(commandname))
      return null;
    return commands.get(commandname);
  }

  public <T> Runnable spawnHandle(CommandRegistry<T> registry, StringView view, Message message) {
    return new CommandInvokeHandle<>(exceptions, registry, message, view);
  }

  @Override
  public void invoke(CommandInvokerContext context) {
    /* LIMITAÇÃO a execução de subcomandos em uma árvore proposital. */
    if (!isReady()) {
      LOGGER.debug("CommandInvoker is invoked, but, is not ready to execute commands. Ignoring.");
      return;
    }
    final Message message = context.getMessage();
    final StringView view = context.getView();
    final CommandRegistry<?> registry = this.spawn(view);
    LOGGER.trace("Process command invoker for message: {}.  and view: {}", message, view);
    if (Objects.isNull(registry))
      return;
    this.runAsync(this.spawnHandle(registry, view, message));
  }

  public void shutdown() {
    if (Objects.isNull(service))
      return;
    LOGGER.info("Closing command service...");
    service.shutdown();
    try {
      if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
        service.shutdownNow();
      }
    } catch (InterruptedException exc) {
      service.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
