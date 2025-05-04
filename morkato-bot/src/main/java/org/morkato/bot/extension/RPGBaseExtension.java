package org.morkato.bot.extension;

import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.registration.AppCommandTree;
import org.morkato.boot.BaseExtension;
import org.morkato.bot.commands.GuildRpgTest;
import org.morkato.bot.commands.McisidInspect;
import org.morkato.bot.exceptions.CommandThrowableException;
import org.morkato.bot.parsers.ArtCommandDataParser;
import org.morkato.bot.parsers.ArtOptionParser;
import org.morkato.bot.slashcommands.TestSlash;
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
    apc.text(new GuildRpgTest())
      .setName("test-guild")
      .queue();
    /* Parsers para argumentos */
    apc.objectParser(new ArtCommandDataParser())
      .queue();
    apc.objectParser(new ArtOptionParser())
      .queue();
    /* Tratamento de erros nos comandos */
    apc.textExceptionHandler(new CommandThrowableException())
      .queue();
    /* Slash Commands */
    apc.slashMapper(new TestMapper())
        .queue();
    apc.slash(new TestSlash())
      .setName("test-slash")
      .setDescription("[Teste] Uma descrição foda")
      .queue();
  }
}
