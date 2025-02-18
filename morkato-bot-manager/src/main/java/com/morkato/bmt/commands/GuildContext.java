package com.morkato.bmt.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public interface GuildContext extends Context {
  Member getMember();
  Guild getGuild();
}
