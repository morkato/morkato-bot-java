package org.morkato.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.morkato.api.entity.trainer.Trainer;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.TextCommandContext;

import java.math.BigDecimal;

@Component
public class TrainerCommand implements Command<Trainer>{
  @Override
  public void invoke(TextCommandContext<Trainer> ctx) throws Throwable {
    ctx.sendMessage("" + ctx.getDefinedArguments()).queue();
    Trainer trainer = ctx.getDefinedArguments();
    int minutes = trainer.getCooldownTimeSeconds();
    int peer =trainer.getCooldownPeer();
    String description = "> **୨ `" + trainer.getEmoji() + "`﹒**";
    description += "Você executou o treino: **" + trainer.getName() + "**.";
    if (minutes != 0)
      description += " Espere " + minutes + " Minutos para executar o próximo treino.";
    if (peer != 0)
      description += " Você só pode executar " + peer + " (ou " + (peer + 1) + ", caso prodígio) treinos por dia.";
    description += "\n\n";
    if (!trainer.getLife().equals(BigDecimal.ZERO))
      description += "> **ɞ `❤`﹒" + trainer.getLife() + "** de Vida";
    MessageEmbed embed = new EmbedBuilder()
      .setTitle("Treino: " + trainer.getName())
      .setDescription(description)
      .setImage(trainer.getBanner())
      .build();
    ctx.createMessage().setEmbeds(embed).queue();
  }
}
