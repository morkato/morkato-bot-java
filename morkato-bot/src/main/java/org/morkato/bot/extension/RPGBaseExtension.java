package org.morkato.bot.extension;

import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.parser.IntegerParser;
import org.morkato.bmt.parser.StringParser;
import org.morkato.bmt.startup.AppCommandTree;
import org.morkato.boot.BaseExtension;
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
    final AppCommandTree apc = ctx.getAppCommandsTree();
    /* Comandos padrões */
    apc.text(new McisidInspect())
      .setName("mcisid")
      .addAlias("mcis")
      .queue();
    apc.text(new TestException())
      .setName("exception")
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
    apc.exceptionHandler(new StupidArgumentExceptionHandler())
        .queue();
    apc.exceptionHandler(new CommandExceptionHandler())
        .queue();
    /* Slash Commands */
    apc.slashMapper(new TestMapper())
        .queue();
    apc.slashMapper(new BotArtCreateSlashMapper())
      .queue();
    apc.slash(new McisidInspect())
      .setName("mcisid")
      .setDescription("[Utilitários] Inspeciona o ID.")
      .queue();
    apc.slash(new TestException())
      .setName("exception")
      .deferReply()
      .queue();
  }
}
