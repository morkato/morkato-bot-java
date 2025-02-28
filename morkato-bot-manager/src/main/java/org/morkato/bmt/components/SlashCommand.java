package org.morkato.bmt.components;

import org.morkato.bmt.context.InteractionCommandContext;

public interface SlashCommand<Args> {
  void invoke(InteractionCommandContext<Args> interaction) throws Throwable;
}
