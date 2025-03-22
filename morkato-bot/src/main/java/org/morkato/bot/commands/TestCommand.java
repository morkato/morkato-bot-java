package org.morkato.bot.commands;

import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.NoArgs;
import org.morkato.bot.parsers.ArtParser;

@Component
public class TestCommand implements Command<NoArgs> {
  @AutoInject
  private ArtParser parser;
  @Override
  public void invoke(TextCommandContext<NoArgs> context) throws Throwable {
    context.sendMessage("Parser injetado: " + parser).queue();
  }
}
