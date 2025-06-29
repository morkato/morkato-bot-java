package org.morkato.bmt.commands.context;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.components.ActionHandler;
import org.morkato.bmt.generated.registries.ActionRegistry;

import java.util.Collection;
import java.util.List;

public interface MessageCreation {
  MessageCreation setContent(String content);
  MessageCreation addEmbed(MessageEmbed embed);
  MessageCreation mentionRepliedUser(boolean m);
  MessageCreation setMessageReference(String id);
  MessageCreation allowedMentions(Collection<Message.MentionType> mentions);
  <T> MessageCreation setActionSession(ActionHandler<T> actionhandler, T payload);
  void queue();

  default <T> MessageCreation setActionSession(ActionRegistry<T> action, T payload) {
    return this.setActionSession(action.getActionHandler(), payload);
  }
  default MessageCreation setMessageReference(Message message) {
    return this.setMessageReference(message.getId());
  }
  default MessageCreation disableMentions() {
    return this.allowedMentions(List.of());
  }
}
