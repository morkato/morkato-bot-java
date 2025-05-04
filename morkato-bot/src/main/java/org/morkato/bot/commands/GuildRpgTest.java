package org.morkato.bot.commands;

import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.repository.guild.GuildCreationQuery;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.boot.annotation.AutoInject;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.NoArgs;
import org.morkato.bmt.context.CommandContext;

public class GuildRpgTest implements CommandHandler<NoArgs> {
  @AutoInject
  private GuildRepository repository;
  @Override
  public void invoke(CommandContext<NoArgs> context) {
    final GuildPayload guild = repository.create(new GuildCreationQuery());
    context.reply()
      .setContent("Guild: " + guild)
      .queue();
  }
}
