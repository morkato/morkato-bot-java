package org.morkato.bmt.invoker;

import org.morkato.bmt.BotCore;
import org.morkato.bmt.commands.CommandInvokeHandle;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.commands.CommandInvokerContext;
import org.morkato.utility.StringView;
import net.dv8tion.jda.api.entities.Message;
import java.util.concurrent.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class CommandInvoker implements Invoker<CommandInvokerContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandInvoker.class);
  private static final int MAX_THREAD_POOL_SIZE = 10;
  private ExecutorService service = null;
  private boolean ready = false;
  private BotCore core;

  @Override
  public boolean isReady() {
    return ready;
  }

  @Override
  public void start(BotCore core) {
    if (ready)
      return;
    Objects.requireNonNull(core);
    final AtomicInteger atomic = new AtomicInteger();
    this.service = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE, runnable -> {
      Thread thread = new Thread(runnable);
      thread.setName("morkato-command-invoker-" + atomic.getAndIncrement());
      return thread;
    });
    this.core = core;
    this.ready = true;
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
    final String name = view.word();
    final CommandRegistry<?> registry = this.spawn(name);
    LOGGER.trace("Process command invoker for message: {}.  and view: {}", message, view);
    if (Objects.isNull(registry))
      return;
    this.runAsync(this.spawnHandle(
      registry, view, name, message));
  }

  @Override
  public void shutdown(){
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

  private CompletableFuture<Void> runAsync(Runnable runnable) {
    LOGGER.trace("Run in async runnable: {}", runnable);
    return CompletableFuture.runAsync(runnable, service);
  }

  private CommandRegistry<?> spawn(String commandname) {
    if (Objects.isNull(commandname))
      return null;
    return core.getTextCommand(commandname);
  }

  private <T> Runnable spawnHandle(
    CommandRegistry<T> registry,
    StringView view,
    String invokedCommandName,
    Message message
  ) {
    return new CommandInvokeHandle<>(
      core, registry, message,
      invokedCommandName, view);
  }
}
