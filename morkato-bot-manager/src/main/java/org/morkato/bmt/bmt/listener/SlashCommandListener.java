package org.morkato.bmt.bmt.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.morkato.bmt.bmt.registration.impl.MorkatoBotManagerRegistration;

public class SlashCommandListener extends MorkatoListenerAdapter {
  public SlashCommandListener(MorkatoBotManagerRegistration registration) {
    super(registration);
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    SlashCommandInteraction interaction = event.getInteraction();
    /* TODO: Adicionar suporte para slash commands, interface: com.morkato.bmt.components.SlashCommand */
    interaction.reply("In development").queue();
    System.out.println(interaction.getCommandString());
    System.out.println(interaction);
    interaction.getChannel().sendMessage("Slash Command DEBUG: " + interaction + "--" + interaction.getName()).queue();
    try {
      Thread.sleep(15);
    } catch (InterruptedException ignored) {}
  }
}
