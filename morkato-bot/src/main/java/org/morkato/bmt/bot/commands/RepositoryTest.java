package org.morkato.bmt.bot.commands;

import org.morkato.bmt.api.entity.ObjectResolver;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.entity.trainer.Trainer;
import org.morkato.bmt.bmt.annotation.AutoInject;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.argument.NoArgs;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.context.TextCommandContext;
import org.morkato.bmt.api.repository.RepositoryCentral;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@MorkatoComponent
public class RepositoryTest implements Command<NoArgs> {
  @AutoInject
  RepositoryCentral repository;
  @Override
  public void invoke(TextCommandContext<NoArgs> ctx) throws Throwable {
    Guild guild = repository.guild().fetch(ctx.getGuild().getId());
//    ctx.sendMessage("" + guild).queue();
//    ctx.sendMessage("Memory Location: " + Integer.toHexString(guild.hashCode())).queue();
//    ObjectResolver<Art> arts = guild.getArtResolver();
//    boolean loaded = arts.loaded();
//    arts.resolve();
//    Art[] resolved = StreamSupport.stream(arts.spliterator(), false).toArray(Art[]::new);
//    ctx.sendMessage((loaded ? "Cached: " : "Resolved: ") + Arrays.toString(resolved)).queue();
    ObjectResolver<Trainer> trainers = guild.getTrainerResolver();
    trainers.resolve();
    Trainer[] resolved = StreamSupport.stream(trainers.spliterator(), false).toArray(Trainer[]::new);
    ctx.sendMessage("" + Arrays.toString(resolved)).queue();
  }
}