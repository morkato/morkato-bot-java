package org.morkato.bmt.internal.action;

import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import org.morkato.bmt.context.CommandContext;
import org.morkato.bmt.action.MessageCreation;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collection;

public class SlashMessageCreationInternal implements MessageCreation {
  private final MessageCreateBuilder builder = new MessageCreateBuilder();
  private final CommandInteraction interaction;

  public SlashMessageCreationInternal(CommandContext<?> ctx) {
    this.interaction = ctx.getInteraction();
  }

  @Override
  public MessageCreation setContent(String content) {
    builder.setContent(content);
    return this;
  }

  @Override
  public MessageCreation addEmbed(MessageEmbed embed) {
    builder.addEmbeds(embed);
    return this;
  }

  @Override
  public MessageCreation mentionRepliedUser(boolean m) {
    builder.mentionRepliedUser(m);
    return this;
  }

  @Override
  public MessageCreation setMessageReference(String id) {
    return this;
  }

  @Override
  public MessageCreation allowedMentions(Collection<Message.MentionType> mentions) {
    builder.setAllowedMentions(mentions);
    return this;
  }

  @Override
  public void queue() {
    final MessageCreateData data = builder.build();
    if (interaction.isAcknowledged()) {
      interaction.getHook().sendMessage(data).queue();
      return;
    }
    interaction.reply(data).queue();
  }
}
