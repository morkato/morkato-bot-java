package org.morkato.bot.extension;

import org.morkato.bot.action.PagedEmbedAction;
import org.morkato.mcbmt.BotContext;
import org.morkato.mcbmt.commands.sharedrules.IntegerParser;
import org.morkato.mcbmt.commands.sharedrules.StringParser;
import org.morkato.mcbmt.startup.builder.AppBuilder;
import org.morkato.boot.BaseExtension;
import org.morkato.bot.action.ResponseTest;
import org.morkato.bot.commands.TestException;
import org.morkato.bot.commands.McisidInspect;
import org.morkato.bot.exceptions.CommandExceptionHandler;
import org.morkato.bot.exceptions.StupidArgumentExceptionHandler;
import org.morkato.bot.slashmapper.TestMapper;

public class RPGBaseExtension extends BaseExtension<BotContext> {
  @Override
  public void setup(BotContext ctx) {
    final AppBuilder apc = ctx.getAppCommandsTree();
    apc.use(new McisidInspect());
    apc.use(new TestException());
    apc.action(new PagedEmbedAction())
      .setId("embed")
      .queue();
    /* Comandos padrões */
    apc.textCommand(McisidInspect.class)
      .setName("mcisid")
      .addAlias("mcis")
      .queue();
    apc.textCommand(TestException.class)
      .setName("paged")
      .queue();
    /* Parsers para argumentos */
    apc.objectParser(new StringParser())
      .queue();
    apc.objectParser(new IntegerParser())
      .queue();
    /* Tratamento de erros nos comandos */
    apc.commandExceptionHandler(new StupidArgumentExceptionHandler())
      .queue();
    apc.commandExceptionHandler(new CommandExceptionHandler())
      .queue();
    /* Slash Commands */
    apc.slashMapper(new TestMapper())
      .queue();
    apc.slashCommand(McisidInspect.class)
      .setName("mcisid")
      .setDescription("[Utilitários] Inspeciona o ID.")
      .queue();
    apc.slashCommand(TestException.class)
      .setName("exception")
      .deferReply()
      .queue();
  }
}
