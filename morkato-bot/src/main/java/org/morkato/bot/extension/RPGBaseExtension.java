package org.morkato.bot.extension;

import org.morkato.bmt.context.BotContext;
import org.morkato.boot.BaseExtension;
import org.morkato.bot.commands.GuildRpgTest;
import org.morkato.bot.commands.McisidInspect;
import org.morkato.bot.exceptions.CommandThrowableException;
import org.morkato.bot.parsers.ArtCommandDataParser;
import org.morkato.bot.parsers.ArtOptionParser;

public class RPGBaseExtension extends BaseExtension<BotContext> {
  @Override
  public void setup(BotContext ctx) throws Throwable {
    /* Comandos padrões */
    ctx.registerCommand(new McisidInspect())
      .addName("mcisid")
      .addName("mcis")
      .queue();
    ctx.registerCommand(new GuildRpgTest())
      .addName("test-guild")
      .queue();
    /* Parsers para argumentos */
    ctx.registerArgument(new ArtCommandDataParser());
    ctx.registerArgument(new ArtOptionParser());
    /* Tratamento de erros nos comandos */
    ctx.registerCommandException(new CommandThrowableException());
  }
}
