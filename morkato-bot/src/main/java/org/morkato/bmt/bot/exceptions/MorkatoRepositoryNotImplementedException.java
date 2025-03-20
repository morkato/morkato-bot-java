package org.morkato.bmt.bot.exceptions;

import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;
import org.morkato.bmt.api.exception.repository.RepositoryNotImplementedException;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.components.CommandException;
import org.morkato.bmt.bmt.context.TextCommandContext;

@MorkatoComponent
public class MorkatoRepositoryNotImplementedException implements CommandException<RepositoryNotImplementedException> {
  @Override
  public void doException(TextCommandContext<?> ctx, RepositoryNotImplementedException exception) {
    ctx.createMessage()
      .setContent("Minha API está fora de serviço no momento. Por tanto, não consigo realizar está operação.")
      .queue();
    new MessageCreateActionImpl(ctx.getChannel());
  }
}
