package org.morkato.bot.parsers;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.errors.ArgumentParserException;
import java.lang.annotation.Annotation;

@MorkatoComponent
public class ArtParser implements ObjectParser<Art> {
  @AutoInject
  public RepositoryCentral central;
  @Override
  public Art parse(TextCommandContext<?> ctx, String text, Annotation[] annotations) throws ArgumentParserException {
    String guildId = ctx.getGuild().getId();
    Guild guild = central.guild().fetch(guildId);
    ObjectResolver<Art> arts = guild.getArtResolver();
    arts.resolve();
    return arts.get(text);
  }
}
