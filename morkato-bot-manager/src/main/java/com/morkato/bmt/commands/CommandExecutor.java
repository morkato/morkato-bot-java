package com.morkato.bmt.commands;

import com.morkato.bmt.function.ExceptionFunction;
import org.apache.commons.lang3.reflect.TypeUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message;
import com.morkato.bmt.impl.ContextImpl;
import com.morkato.bmt.NoArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.TypeVariable;
import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
  private static interface SpecialConsumer {
    void accept(MessageReceivedEvent event, StringView view);
  }
  private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
  private final char specialCharacter = ':';
  private final Map<String, SpecialConsumer> specials = new HashMap<>();
  private final ArgumentParser parser = new ArgumentParser();
  private final CommandManager manager;
  public boolean ready = false;
  public CommandExecutor(
    @Nonnull CommandManager manager
  ) {
    this.manager = manager;
    specials.put("id", this::onIdGet);
    specials.put("rn", this::onRunCmd);
    specials.put(":", this::onRunCmd);
    specials.put("pn", this::onParCmd);
  }

  private void onError(Context<?> ctx, Throwable exc) {
    logger.error("An unexpected error occurred: **{}** in command: {}.", exc.getClass().getName(), ctx.getCommand().getClass().getName());
    exc.printStackTrace();
    ctx.send("An unexpected error occurred: **" + exc.getClass().getName() + "** in command ID: **" + ctx.getCommand().getClass().getName() + "**. For more dev details watch my logs in my process.").queue();
  }

  private void onIdGet(MessageReceivedEvent event, StringView view) {
    String commandname = view.word();
    Class<?> clazz = manager.getNameValue(commandname);
    event.getMessage().reply("ID of command: **`" + clazz.getName() + "`**").queue();
  }
  private void onRunCmd(MessageReceivedEvent event, StringView view) {
    Message message = event.getMessage();
    String classpath = view.word();
    try {
      Class<?> clazz = Class.forName(classpath);
      Command<?> command = manager.getCommandInstance(clazz);
      if (command == null) {
        message.reply("This Java Class is not a Command!").queue();
      }
      this.runCommand(command, view, message);
    } catch (ClassNotFoundException exc) {
      message.reply("Java Class not found!").queue();
    }
  }

  private <T> void onParCmd(MessageReceivedEvent event, StringView view) {
    Message message = event.getMessage();
    String classpath = view.word();
    if (classpath == null) {
      message.reply("It's necessary specifics a java class.").mentionRepliedUser(false).queue();
      return;
    }
    try {
      Class<?> clazz =  Class.forName(classpath);
      Object parsed = this.parser.parse(clazz, view);
      if (parsed == null) {
        message.reply("```\nClass parsed :"  + clazz.getName() + ": <unknown parse>```").mentionRepliedUser(false).queue();
        return;
      }
      message.reply("```\nClass parsed :"  + clazz.getName() + ": " + parsed.toString() + "```").mentionRepliedUser(false).queue();
    } catch (ClassNotFoundException exc) {
      message.reply("Error: " + classpath + " class is not found.").mentionRepliedUser(false).queue();
    }
  }

  public boolean isReady() {
    return this.ready;
  }
  public void setReady() {
    logger.info("CommandExecutor is ready to execute commands.");
    this.ready = true;
  }

  @SuppressWarnings("unchecked")
  public <E extends Throwable> void handleError(Context<?> ctx, E exc) {
    ExceptionFunction<E> consumer = (ExceptionFunction<E>) manager.getExceptionHandler(exc.getClass());
    if (consumer == null)
      consumer = this::onError;
    consumer.accept(ctx, exc);
  }

  public <T> void runCommand(
    @Nonnull Command<T> command,
    @Nonnull StringView view,
    @Nonnull Message message
  ) {
    Map<TypeVariable<?>,Type> typeArguments = TypeUtils.getTypeArguments(command.getClass(), Command.class);
    Class<T> argsClazz = (Class<T>) typeArguments.values().iterator().next();
    T object = null;
    if (argsClazz != NoArgs.class) {
      object = this.parser.parse(argsClazz, view);
    }
    Context<T> context = new ContextImpl<>(command, message, object);
    try {
      command.invoke(context);
    } catch (Throwable exc) {
      this.handleError(context, exc);
    }
  }

  public void invoke(MessageReceivedEvent event) {
    Message message = event.getMessage();
    String prefix = manager.getPrefix();
    if (prefix == null || !isReady() || message.getAuthor().isBot())
      return;
    StringView view = new StringView(message.getContentRaw());
    String supposedPrefix = view.read(prefix.length());
    if (!prefix.equals(supposedPrefix))
      return;
    char special = view.read(1).charAt(0);
    if (special == specialCharacter) {
      String specialname = view.word();
      SpecialConsumer consumer = this.specials.get(specialname);
      if (consumer != null)
        consumer.accept(event, view);
      return;
    }
    view.undo();
    String commandname = view.word();
    if (commandname == null)
      return;
    Command<?> command = manager.getCommandByName(commandname);
    if (command == null) {
      logger.debug("Usuário: {} (ID: ) invocou um comando no qual não reconheço (Não registrado): {}.",
        event.getAuthor().getName(), event.getAuthor().getId(), commandname);
      return;
    }
    this.runCommand(command, view, message);
  }
}
