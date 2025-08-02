package org.morkato.mcbmt.commands.context;

import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.morkato.mcbmt.commands.CommandContext;
import org.morkato.mcbmt.actions.LayoutAction;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collection;

public class SlashMessageCreation<ContextType>
  extends SessionableMessageCreation<ContextType>
  implements MessageCreation<ContextType> {
  private final MessageCreateBuilder builder = new MessageCreateBuilder();
  private final CommandInteraction interaction;

  public SlashMessageCreation(CommandContext<ContextType> ctx) {
    super(ctx);
    this.interaction = ctx.getInteraction();
  }

  @Override
  protected void syncBuilderWithLayout(LayoutAction layout){
    builder.addComponents(layout.build());
  }

  private void dispatchQuotedHook(InteractionHook hook) {
    hook.retrieveOriginal().queue(this::dispatchQueuedMessage);
  }

  @Override
  public MessageCreation<ContextType> setContent(String content) {
    builder.setContent(content);
    return this;
  }

  @Override
  public MessageCreation<ContextType> addEmbed(MessageEmbed embed) {
    builder.addEmbeds(embed);
    return this;
  }

  @Override
  public MessageCreation<ContextType> mentionRepliedUser(boolean m) {
    builder.mentionRepliedUser(m);
    return this;
  }

  @Override
  public MessageCreation<ContextType> setMessageReference(String id) {
    return this;
  }

  @Override
  public MessageCreation<ContextType> allowedMentions(Collection<Message.MentionType> mentions) {
    builder.setAllowedMentions(mentions);
    return this;
  }

  @Override
  public void queue() {
    final MessageCreateData data = builder.build();
    if (interaction.isAcknowledged()) {
      interaction.getHook().sendMessage(data).queue(this::dispatchQueuedMessage);
      return;
    }
    interaction.reply(data).queue(this::dispatchQuotedHook);
  }
}
