package org.morkato.bmt;

import org.morkato.bmt.commands.CommandExecutor;
import org.morkato.bmt.management.CommandManager;
import org.morkato.bmt.components.Extension;
import org.morkato.bmt.management.ComponentManager;
import org.morkato.bmt.management.ExtensionManager;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;
import org.morkato.utility.ClassInjectorMap;
import org.morkato.utility.exception.ValueAlreadyInjected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

public class BotListener extends ListenerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(BotListener.class);
  private final CommandExecutor executor;

  public BotListener(CommandExecutor executor) {
    this.executor = executor;
  }
  @Override
  public void onReady(ReadyEvent event) {
    /* Carregado quando o bot faz login! Sendo assim, é carregado todas as extensões, errors bouders e comandos! */
    logger.info("Estou conectado, como: {}", event.getJDA().getSelfUser().getAsTag());
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    /* Yup, yup! Lê a mensagem recebida e responde o usuário, caso o bot reconheça o comando, e caso ele esteja pronto para executar comandos! */
    if (!executor.isReady())
      return;
    executor.invoke(event);
  }
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    SlashCommandInteraction interaction = event.getInteraction();
    /* TODO: Adicionar suporte para slash commands, interface: com.morkato.bmt.components.SlashCommand */
  }

  @Override
  public void onShutdown(@NotNull ShutdownEvent event) {
    logger.debug("Fechando o executor.");
  }
}
