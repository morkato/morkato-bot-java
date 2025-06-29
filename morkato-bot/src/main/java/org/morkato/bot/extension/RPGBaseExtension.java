package org.morkato.bot.extension;

import org.morkato.bmt.BotContext;
import org.morkato.bmt.commands.sharedrules.IntegerParser;
import org.morkato.bmt.commands.sharedrules.StringParser;
import org.morkato.bmt.startup.builder.AppBuilder;
import org.morkato.boot.BaseExtension;
import org.morkato.bot.action.ResponseTest;
import org.morkato.bot.commands.TestException;
import org.morkato.bot.commands.McisidInspect;
import org.morkato.bot.exceptions.CommandExceptionHandler;
import org.morkato.bot.exceptions.StupidArgumentExceptionHandler;
import org.morkato.bot.parsers.ArtCommandDataParser;
import org.morkato.bot.parsers.ArtOptionParser;
import org.morkato.bot.slashmapper.BotArtCreateSlashMapper;
import org.morkato.bot.slashmapper.TestMapper;

public class RPGBaseExtension extends BaseExtension<BotContext> {
  @Override
  public void setup(BotContext ctx) throws Throwable {
    final AppBuilder apc = ctx.getAppCommandsTree();
    apc.use(new McisidInspect());
    apc.use(new TestException());
    apc.action(new ResponseTest())
      .setId("test")
      .queue();
    /* Comandos padrões */
    apc.textCommand(McisidInspect.class)
      .setName("mcisid")
      .addAlias("mcis")
      .queue();
    apc.textCommand(TestException.class)
      .setName("interaction")
      .queue();
    /* Parsers para argumentos */
    apc.objectParser(new StringParser())
        .queue();
    apc.objectParser(new IntegerParser())
        .queue();
    apc.objectParser(new ArtCommandDataParser())
      .queue();
    apc.objectParser(new ArtOptionParser())
      .queue();
    /* Tratamento de erros nos comandos */
    apc.commandExceptionHandler(new StupidArgumentExceptionHandler())
        .queue();
    apc.commandExceptionHandler(new CommandExceptionHandler())
        .queue();
    /* Slash Commands */
    apc.slashMapper(new TestMapper())
        .queue();
    apc.slashMapper(new BotArtCreateSlashMapper())
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
