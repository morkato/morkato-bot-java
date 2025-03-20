package org.morkato.bmt.bmt.invoker;

import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.bmt.commands.CommandRegistry;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.context.invoker.CommandInvokerContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Objects;

import org.morkato.bmt.bmt.registration.TextCommandRegistration;
import org.morkato.bmt.bmt.registration.impl.MorkatoBotManagerRegistration;
import org.morkato.bmt.utility.StringView;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class CommandInvoker implements Invoker<CommandInvokerContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandInvoker.class);
  private static final int MAX_THREAD_POOL_SIZE = 10;
  private final MorkatoBotManagerRegistration registration;
  private final TextCommandRegistration commands;
  private ExecutorService service = null;
  private boolean ready = false;

  public CommandInvoker(MorkatoBotManagerRegistration registration) {
    this.registration = registration;
    this.commands = registration.getTextCommandRegistration();
  }

  public synchronized void start() {
    if (ready)
      return;
    this.service = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
    this.ready = true;
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  public CompletableFuture<Void> runAsync(Runnable runnable) {
    return CompletableFuture.runAsync(runnable, service);
  }

  @SuppressWarnings("unchecked")
  public CommandRegistry<?> spawn(StringView view) {
    final String commandname = view.word();
    if (Objects.isNull(commandname))
      return null;
    try {
      return commands.getRegistry((Class<? extends Command<?>>)Class.forName(commandname).asSubclass(Command.class));
    } catch (Exception ignored) {}
    return null;
  }

  @Override
  public void invoke(CommandInvokerContext context) {
    if (!isReady())
      return;
    final Message message = context.getMessage();
    final StringView view = context.getView();
    final CommandRegistry<?> registry = this.spawn(view);
    if (Objects.isNull(registry))
      return;
    final Runnable runnable = registry.prepareRunnable(message, view);
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
