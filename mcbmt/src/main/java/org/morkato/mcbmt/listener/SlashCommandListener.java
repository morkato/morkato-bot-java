package org.morkato.mcbmt.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.morkato.mcbmt.invoker.Invoker;
import javax.annotation.Nonnull;

public class SlashCommandListener extends ListenerAdapter {
  private final Invoker<SlashCommandInteractionEvent> invoker;

  public SlashCommandListener(Invoker<SlashCommandInteractionEvent> invoker) {
    this.invoker = invoker;
  }

  @Override
  public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
    if (!invoker.isReady())
      return;
    invoker.invoke(event);
  }
}
