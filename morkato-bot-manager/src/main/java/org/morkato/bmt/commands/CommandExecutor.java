package org.morkato.bmt.commands;

import org.morkato.bmt.function.DevToolConsumer;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.bmt.management.CommandExceptionManager;
import org.morkato.bmt.management.CommandManager;
import org.morkato.bmt.management.DevToolsManager;
import org.morkato.utility.StringView;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
  private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
  public static final char SPECIAL_CHARACTER = ':';
  public static final int THREAD_POOL_SIZE = 10;
  private final Map<String,DevToolConsumer> specials = new HashMap<>();
  private ExecutorService executor = null;
  private final ArgumentManager arguments;
  private final DevToolsManager devtools;
  private final CommandManager manager;
  public boolean ready = false;
  private String prefix;
  public CommandExecutor(
    @Nonnull CommandManager manager,
    @Nonnull ArgumentManager arguments
    ) {
    this.manager = manager;
    this.devtools = DevToolsManager.get(this);
    this.arguments = arguments;
  }

  public String getPrefix() {
    return this.prefix;
  }
  public void setPrefix(
    @Nonnull String newvl
  ) {
    this.prefix = newvl;
  }

  @Nonnull
  public CommandManager getCommandManager() {
    return manager;
  }

  public boolean isReady() {
    return this.ready;
  }
  public void setReady() {
    logger.info("{} is ready to execute commands.", this.getClass().getSimpleName());
    Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    this.ready = true;
  }

  public CompletableFuture<Void> runAsync(Runnable runnable) {
    return CompletableFuture.runAsync(runnable, executor);
  }

  public DevToolConsumer processDevTool(StringView view) {
    char supposedSpecialCharacter = view.current();
    if (supposedSpecialCharacter != SPECIAL_CHARACTER) {
      return null;
    }
    view.get();
    String specialname = view.word();
    return devtools.getDevTool(specialname);
  }
  public CommandRegistry<?> processCommand(StringView view) {
    String commandname = view.word();
    if (commandname == null)
      return null;
    return manager.getRegistryByName(commandname);
  }
  public Runnable spawn(MessageReceivedEvent event, StringView view) {
    DevToolConsumer devtool = this.processDevTool(view);
    if (devtool != null)
      return () -> devtool.accept(event, view);
    CommandRegistry<?> registry = this.processCommand(view);
    if (registry == null)
      return null;
    return registry.prepareRunnable(event.getMessage(), arguments, view);
  }
  public StringView process(MessageReceivedEvent event) {
    Message message = event.getMessage();
    String prefix = this.prefix;
    if (prefix == null || !isReady() || message.getAuthor().isBot())
      return null;
    StringView view = new StringView(message.getContentRaw());
    String supposedPrefix = view.read(prefix.length());
    if (!prefix.equals(supposedPrefix))
      return null;
    return view;
  }

  public void invoke(MessageReceivedEvent event) {
    StringView view = this.process(event);
    if (view == null)
      return;
    Runnable spawned = this.spawn(event, view);
    if (spawned == null)
      return;
    this.runAsync(spawned);
  }

  public void shutdown() {
    logger.info("Closing command executor...");
    if (Objects.isNull(executor))
      return;
    executor.shutdown();
    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException exc) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
