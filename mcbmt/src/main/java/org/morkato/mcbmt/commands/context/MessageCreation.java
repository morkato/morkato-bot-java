package org.morkato.mcbmt.commands.context;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.generated.registries.ActionRegistry;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MessageCreation<ContextType> {
  MessageCreation<ContextType> setContent(String content);
  MessageCreation<ContextType> addEmbed(MessageEmbed embed);
  MessageCreation<ContextType> mentionRepliedUser(boolean m);
  MessageCreation<ContextType> setMessageReference(String id);
  MessageCreation<ContextType> allowedMentions(Collection<Message.MentionType> mentions);
  <T> MessageCreation<ContextType> setActionSession(ActionHandler<T> actionhandler, T payload);
  MessageCreation<ContextType> dispatch(BiConsumer<CommandContext<ContextType>, Message> dispatcher);
  void queue();

  default <T> MessageCreation<ContextType> setActionSession(ActionRegistry<T> action, T payload) {
    return this.setActionSession(action.getActionHandler(), payload);
  }
  default MessageCreation<ContextType> setMessageReference(Message message) {
    return this.setMessageReference(message.getId());
  }
  default MessageCreation<ContextType> disableMentions() {
    return this.allowedMentions(List.of());
  }
}
