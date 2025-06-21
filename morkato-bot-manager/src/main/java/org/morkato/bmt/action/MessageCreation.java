package org.morkato.bmt.action;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Message;
import java.util.Collection;
import java.util.List;

public interface MessageCreation {
  MessageCreation setContent(String content);
  MessageCreation addEmbed(MessageEmbed embed);
  MessageCreation mentionRepliedUser(boolean m);
  MessageCreation setMessageReference(String id);
  MessageCreation allowedMentions(Collection<Message.MentionType> mentions);
  void queue();

  default MessageCreation setMessageReference(Message message) {
    return this.setMessageReference(message.getId());
  }
  default MessageCreation disableMentions() {
    return this.allowedMentions(List.of());
  }
}
