package org.morkato.bmt.bmt.components;

import org.morkato.bmt.bmt.context.InteractionCommandContext;

public interface SlashCommand<Args> {
  void invoke(InteractionCommandContext<Args> interaction) throws Throwable;
}
