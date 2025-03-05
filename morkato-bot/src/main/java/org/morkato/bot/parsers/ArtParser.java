package org.morkato.bot.parsers;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.ObjectParserContext;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.ArgumentParserException;
import org.morkato.bot.exceptions.MorkatoBotException;
import org.morkato.bot.parsers.util.ArtMapByName;
import java.lang.annotation.Annotation;
import java.util.Objects;

@MorkatoComponent
public class ArtParser implements ObjectParser<Art> {
  ArtMapByName cachedArts = new ArtMapByName();
  @AutoInject
  public RepositoryCentral central;
  @Override
  public Art parse(ObjectParserContext objctx) throws ArgumentParserException {
    TextCommandContext<?> ctx = objctx.getContext();
    String guildId = ctx.getGuild().getId();
    Guild guild = central.guild().fetch(guildId);
    ObjectResolver<Art> arts = guild.getArtResolver();
    arts.resolve();
    Art art = cachedArts.get(guild, objctx.getText());
    if (Objects.isNull(art))
      throw new RuntimeException("A arte (Respiração, Kekkijutsu ou Estilo de Luta): **" + objctx.getText() + "** não existe.");
    return art;
  }
}
