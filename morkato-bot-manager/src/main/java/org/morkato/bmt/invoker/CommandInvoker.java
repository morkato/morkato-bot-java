package org.morkato.bmt.invoker;

import org.morkato.bmt.CommandRegistry;
import org.morkato.bmt.context.invoker.CommandInvokerContext;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.utility.StringView;
import net.dv8tion.jda.api.entities.Message;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class CommandInvoker implements Invoker<CommandInvokerContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandInvoker.class);
  private static final int MAX_THREAD_POOL_SIZE = 10;
  private MapRegistryManagement<String, CommandRegistry<?>> commands;
  private ExecutorService service = null;
  private boolean ready = false;

  public synchronized void start(MapRegistryManagement<String, CommandRegistry<?>> commands) {
    if (ready)
      return;
    this.service = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
    this.commands = commands;
    this.ready = true;
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  public CompletableFuture<Void> runAsync(Runnable runnable) {
    return CompletableFuture.runAsync(runnable, service);
  }

  public CommandRegistry<?> spawn(StringView view) {
    final String commandname = view.word();
    if (Objects.isNull(commandname))
      return null;
    return commands.get(commandname);
  }

  @Override
  public void invoke(CommandInvokerContext context) {
    /* LIMITAÇÃO a execução de subcomandos em uma árvore proposital. */
    if (!isReady())
      return;
    final Message message = context.getMessage();
    final StringView view = context.getView();
    final CommandRegistry<?> registry = this.spawn(view);
    if (Objects.isNull(registry))
      return;
    if (!registry.hasSubCommands()) {
      final Runnable runnable = registry.prepareRunnable(message, view);
      this.runAsync(runnable);
      return;
    }
    view.skipWhitespace();
    final String supposedSubCommandName = view.quotedWord();
    final CommandRegistry<?> subregistry = registry.getSubCommand(supposedSubCommandName);
    if (Objects.isNull(subregistry)) {
      final Runnable runnable = registry.prepareRunnable(message, view);
      this.runAsync(runnable);
      view.undo();
      return;
    }
    final Runnable runnable = subregistry.prepareRunnable(message, view);
    this.runAsync(runnable);
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
