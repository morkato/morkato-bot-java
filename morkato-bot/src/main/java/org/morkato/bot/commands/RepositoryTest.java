package org.morkato.bot.commands;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.guild.Guild;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.argument.NoArgs;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.api.repository.RepositoryCentral;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class RepositoryTest implements Command<NoArgs> {
  @AutoInject
  RepositoryCentral repository;
  @Override
  public void invoke(TextCommandContext<NoArgs> ctx) throws Throwable {
    Guild guild = repository.guild().fetch(ctx.getGuild().getId());
    ctx.send("" + guild).queue();
    ctx.send("Memory Location: " + Integer.toHexString(guild.hashCode())).queue();
    ObjectResolver<Art> arts = guild.getArtResolver();
    boolean loaded = arts.loaded();
    arts.resolve();
    Art[] resolved = StreamSupport.stream(arts.spliterator(), false).toArray(Art[]::new);
    ctx.send((loaded ? "Cached: " : "Resolved: ") + Arrays.toString(resolved)).queue();
  }
}