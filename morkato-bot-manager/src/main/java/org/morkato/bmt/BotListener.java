package org.morkato.bmt;

import org.morkato.bmt.commands.CommandExecutor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotListener extends ListenerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(BotListener.class);
  private final CommandExecutor executor;

  public BotListener(CommandExecutor executor) {
    this.executor = executor;
  }
  @Override
  public void onReady(ReadyEvent event) {
    /* Carregado quando o bot faz login! Sendo assim, é carregado todas as extensões, errors bouders e comandos! */
    logger.info("Estou conectado, como: {}", event.getJDA().getSelfUser().getName());
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
