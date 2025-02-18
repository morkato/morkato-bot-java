package com.morkato.bot.commands;

import com.morkato.bmt.annotation.ExtensionRegistry;
import com.morkato.bmt.commands.Command;
import com.morkato.bmt.commands.Context;
import com.morkato.bmt.NoArgs;
import com.morkato.bmt.commands.Interaction;
import com.morkato.bmt.commands.SlashCommand;

@ExtensionRegistry(extension = com.morkato.bot.extension.Extension.class)
public class Test implements Command<NoArgs>, SlashCommand<NoArgs> {
  @Override
  public void invoke(Context ctx) throws Throwable {
    ctx.send("Comando de teste em java!").queue();
    ctx.getArgs();
  }

  @Override
  public void invoke(Interaction<NoArgs> interaction) throws Throwable {
    var a = interaction.getArgs();
  }
}
