package org.morkato.bmt.components;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.morkato.bmt.commands.CommandExecutor;
import org.morkato.utility.StringView;

public interface DevTool {
  void doExecute(CommandExecutor executor, MessageReceivedEvent event, StringView view);
}
