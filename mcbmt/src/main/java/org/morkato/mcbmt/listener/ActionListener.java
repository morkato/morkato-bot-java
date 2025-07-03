package org.morkato.mcbmt.listener;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.morkato.mcbmt.invoker.Invoker;

public class ActionListener extends ListenerAdapter {
  private final Invoker<ButtonInteractionEvent> invoker;

  public ActionListener(Invoker<ButtonInteractionEvent> invoker) {
    this.invoker = invoker;
  }

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    if (!invoker.isReady())
      return;
    invoker.invoke(event);
  }
}
