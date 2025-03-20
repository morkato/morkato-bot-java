package org.morkato.bmt.bot.parsers;

import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.art.Art;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.repository.RepositoryCentral;
import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.bmt.components.ObjectParser;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.annotation.AutoInject;
import org.morkato.bmt.bmt.errors.ArgumentParserException;
import org.morkato.bmt.bot.parsers.util.ArtMapByName;
import java.util.Objects;

@MorkatoComponent
public class ArtParser implements ObjectParser<Art> {
  ArtMapByName cachedArts = new ArtMapByName();
  @AutoInject
  public RepositoryCentral central;
  @Override
  public Art parse(TextCommandContext<?> ctx, String text) throws ArgumentParserException {
    String guildId = ctx.getGuild().getId();
    Guild guild = central.guild().fetch(guildId);
    ObjectResolver<Art> arts = guild.getArtResolver();
    arts.resolve();
    Art art = cachedArts.get(guild, text);
    if (Objects.isNull(art))
      throw new RuntimeException("A arte (Respiração, Kekkijutsu ou Estilo de Luta): **" + text + "** não existe.");
    return art;
  }
}
