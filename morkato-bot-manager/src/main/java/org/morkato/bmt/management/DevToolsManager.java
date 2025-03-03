package org.morkato.bmt.management;

import org.morkato.bmt.argument.ArgumentParser;
import org.morkato.bmt.commands.CommandExecutor;
import org.morkato.bmt.commands.CommandRegistry;
import org.morkato.utility.StringView;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.function.DevToolConsumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DevToolsManager {
  private final Map<String,DevToolConsumer> devtools = new HashMap<>();
  private final CommandManager commands;
  private final ArgumentParser parser;
  private final CommandExecutor executor;
  @Nonnull
  public static DevToolsManager get(
    @Nonnull CommandExecutor executor
  ) {
    DevToolsManager devtools = new DevToolsManager(executor);
    devtools.register("rn", devtools::onRunCommandTool);
    devtools.register("ri", devtools::onCommandNameInfo);
//    devtools.register("pn", devtools::onParameterCheckTool);
    devtools.register("tmc", devtools::onTimerCommand);
    return devtools;
  }
  public DevToolsManager(
    @Nonnull CommandExecutor executor
  ) {
    this.executor = executor;
    this.commands = executor.getCommandManager();
    this.parser = executor.getArgumentParser();
  }
  public DevToolConsumer getDevTool(
    @Nonnull String name
  ) {
    return this.devtools.get(name);
  }
  public void register(
    @Nonnull String name,
    @Nonnull DevToolConsumer devtool
  ) {
    this.devtools.put(name, devtool);
  }

  public void onRunCommandTool(MessageReceivedEvent event, StringView view) {
    Message message = event.getMessage();
    String classpath = view.word();
    try {
      Class<?> clazz = Class.forName(classpath);
      CommandRegistry<?> command = commands.getRegistry((Class<? extends Command>)clazz);
      if (command == null) {
        message.reply("This Java Class is not a Command!").queue();
        return;
      }
      view.skipWhitespace();
      Runnable spawned = command.prepareRunnable(message, parser, view);
      spawned.run();
    } catch (ClassNotFoundException exc) {
      message.reply("Java Class not found!").queue();
    }
  }

//  public void onParameterCheckTool(MessageReceivedEvent event, StringView view) {
//    Message message = event.getMessage();
//    String classpath = view.word();
//    if (classpath == null) {
//      message.reply("It's necessary specifics a java class.").mentionRepliedUser(false).queue();
//      return;
//    }
//    try {
//      Class<?> clazz =  Class.forName(classpath);
//      Object parsed = this.parser.parse(clazz, view);
//      if (parsed == null) {
//        message.reply("```\nClass parsed :"  + clazz.getName() + ": <unknown parse>```").mentionRepliedUser(false).queue();
//        return;
//      }
//      message.reply("```\nClass parsed :"  + clazz.getName() + ": " + parsed.toString() + "```").mentionRepliedUser(false).queue();
//    } catch (ClassNotFoundException exc) {
//      message.reply("Error: " + classpath + " class is not found.").mentionRepliedUser(false).queue();
//    } catch (Throwable exc) {}
//  }

  public void onCommandNameInfo(MessageReceivedEvent event, StringView view) {
    view.skipWhitespace();
    char supposedCharacter = view.current();
    CommandRegistry<?> registry = null;
    if (supposedCharacter == CommandExecutor.SPECIAL_CHARACTER) {
      view.get();
      String commandclazz = view.word();
      try {
        registry = commands.getRegistry((Class<? extends Command>) Class.forName(commandclazz));
      } catch (ClassNotFoundException exc) {
      }
    } else {
      String commandname = view.word();
      registry = commands.getRegistryByName(commandname);
    }
    if (registry == null) {
      event.getMessage().reply("Este registro não existe.").mentionRepliedUser(false).queue();
      return;
    }
    String content = "Registro do Comando: **" + registry.getCommandClassName() + "**.";
    Set<String> names = commands.getAllNames(registry.getCommandClass());
    if (!names.isEmpty()) {
      content += " Nomes que apontam para este comando: **" + Arrays.toString(names.toArray()) + "**.";
    }
    content += " Argumentos (Transformador): **" + registry.getArgumentClassName() + "**.";
    event.getMessage().reply(content).mentionRepliedUser(false).queue();
  }

  private void onTimerCommand(MessageReceivedEvent event, StringView view) {
    long processSnapshot = System.nanoTime();
    view.skipWhitespace();
    Runnable spawned = this.executor.spawn(event, view);
    if (spawned == null)
      return;
    BigDecimal processTime = BigDecimal.valueOf(System.nanoTime() - processSnapshot)
      .divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP);
    long commandProcessSnapshot = System.nanoTime();
    CompletableFuture<Void> future = this.executor.runAsync(spawned);
    try {
      future.get();
    } catch (Throwable exc) {}
    BigDecimal commandProcessTime = BigDecimal.valueOf(System.nanoTime() - commandProcessSnapshot)
      .divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP);
    event.getChannel().sendMessage("Tempo do processamento do comando (Precisão em ns: nanosegundo convertido para ms: milisegundo): **" + (processTime.add(commandProcessTime)) + "ms** \nTempo de processamento síncrono: **" + processTime + "ms**.\nTempo de finalização assíncrona do comando: **" + commandProcessTime + "ms**.").queue();
  }
}
