package org.morkato.bmt.components;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface RawSlashCommand {
  void apply(CommandData command);
  void slash(SlashCommandInteraction interaction);
}
