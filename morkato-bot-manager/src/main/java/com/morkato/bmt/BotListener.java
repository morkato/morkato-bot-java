package com.morkato.bmt;

import com.morkato.bmt.commands.CommandExecutor;
import com.morkato.bmt.commands.CommandManager;
import com.morkato.bmt.commands.Command;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BotListener extends ListenerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(BotListener.class);
  private final CommandManager commands;
  private final CommandExecutor executor;
  private final ExtensionManager manager;
  private final Map<Class<?>, Object> injected;
  private Set<Extension> loadedExtensions;

  public BotListener(
    @Nonnull ExtensionManager manager,
    @NotNull CommandManager commands,
    @Nonnull Map<Class<?>, Object> injected
  ) {
    this.executor = new CommandExecutor(commands);
    this.commands = commands;
    this.manager = manager;
    this.injected = injected;
  }

  @Override
  public void onReady(ReadyEvent event) {
    SelfUser user = event.getJDA().getSelfUser();
    injected.put(SelfUser.class, user);
    Set<Extension> extensions = new HashSet<>();
    manager.start(extensions);
    for (Extension extension : extensions) {
      Set<Class<? extends Command>> unlCommands = manager.getAllCommandsFrom(extension.getClass());
      logger.debug("Registering for extension: {}.", extension.getClass().getName());
      for (Class<? extends Command> commandClazz : unlCommands) {
        try {
          Command<?> command = commandClazz.getDeclaredConstructor().newInstance();
          commands.registry(command);
        } catch (NoSuchMethodException exc) {
          logger.warn("Error loading command: {}. No default constructor found.", commandClazz.getName());
        } catch (ReflectiveOperationException exc) {
          logger.warn("Error loading command: {}. Unexpected reflection error: {}", commandClazz.getName(), exc.getMessage());
        }
      }
      extension.setup(commands);
    }
    logger.info("Estou conectado, como: {}", user.getAsTag());
    executor.setReady();
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    /* Yup, yup! Lê a mensagem recebida e responde o usuário, caso o bot reconheça o comando! */
    executor.invoke(event);
  }
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    SlashCommandInteraction interaction = event.getInteraction();
    /* TODO: Adicionar suporte para slash commands, interface: com.morkato.bmt.commands.SlashCommand */
  }
}
