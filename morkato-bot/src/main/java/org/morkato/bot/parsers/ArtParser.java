package org.morkato.bot.parsers;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.repository.guilld.GuildIdQuery;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.exception.ArgumentParserException;
import org.morkato.bot.parsers.util.ArtMapByName;
import java.util.Objects;

@Component
public class ArtParser implements ObjectParser<Art> {
  ArtMapByName cachedArts = new ArtMapByName();
  @AutoInject
  public RepositoryCentral central;
  @Override
  public Art parse(TextCommandContext<?> ctx, String text) throws ArgumentParserException {
    String guildId = ctx.getGuild().getId();
    Guild guild = central.fetchGuild(new GuildIdQuery(guildId));
    ObjectResolver<Art> arts = guild.getArtResolver();
    arts.resolve();
    Art art = cachedArts.get(guild, text);
    if (Objects.isNull(art))
      throw new RuntimeException("A arte (Respiração, Kekkijutsu ou Estilo de Luta): **" + text + "** não existe.");
    return art;
  }
}
