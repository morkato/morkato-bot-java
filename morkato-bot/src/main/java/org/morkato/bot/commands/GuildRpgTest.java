package org.morkato.bot.commands;

import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.repository.guild.GuildCreationQuery;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.boot.annotation.AutoInject;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.NoArgs;
import org.morkato.bmt.context.TextCommandContext;

public class GuildRpgTest implements Command<NoArgs> {
  @AutoInject
  private GuildRepository repository;
  @Override
  public void invoke(TextCommandContext<NoArgs> context) {
    final GuildPayload guild = repository.create(new GuildCreationQuery());
    context.sendMessage("Guild: " + guild).queue();
  }
}
