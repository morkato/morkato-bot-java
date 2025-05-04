package org.morkato.bmt.invoker;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlashCommandInvoker implements Invoker<SlashCommandInteractionEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SlashCommandInvoker.class);
  private MapRegistryManagement<String, SlashCommandRegistry<?>> registries;
  private boolean ready = false;

  public synchronized void start(MapRegistryManagement<String, SlashCommandRegistry<?>> registries) {
    if (ready)
      return;
    this.registries = registries;
    ready = true;
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  @Override
  public void invoke(SlashCommandInteractionEvent event) {
    if (!isReady()) {
      LOGGER.debug("SlashCommandInvoker is invoked, but, is not ready to execute slashcommands. Ignoring.");
      return;
    }
    SlashCommandRegistry<?> registry = registries.get(event.getName());
    try {
      registry.invoke(event);
    } catch (Throwable exc) {
      event.reply("An unexpected error occurred!\nCommandRegistry: **" + registry + "**\nChain with (raw): ** " + registry.getCommandClassName() + "\nWith invoked name: **" + registry.getName() + "**").queue();
    }
  }
}
