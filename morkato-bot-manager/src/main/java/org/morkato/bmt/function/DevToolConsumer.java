package org.morkato.bmt.function;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.morkato.utility.StringView;

public interface DevToolConsumer {
  void accept(MessageReceivedEvent event, StringView view);
}
