package org.morkato.bot.action;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.morkato.bot.PagedEmbed;
import org.morkato.mcbmt.actions.ActionContext;
import org.morkato.mcbmt.actions.ActionSession;
import org.morkato.mcbmt.actions.ButtonAction;
import org.morkato.mcbmt.actions.LayoutAction;
import org.morkato.mcbmt.components.ActionHandler;

public class PagedEmbedAction implements ActionHandler<PagedEmbed> {
  private int currentPage = 0;

  protected static ButtonAction nextPageButton = ButtonAction.builder()
    .emoji((EmojiUnion)Emoji.fromUnicode("▶\uFE0F"))
    .style(ButtonStyle.PRIMARY)
    .build();
  protected static ButtonAction downPageButton = ButtonAction.builder()
    .emoji((EmojiUnion)Emoji.fromUnicode("◀\uFE0F"))
    .style(ButtonStyle.PRIMARY)
    .build();

  @Override
  public void registerAction(LayoutAction layout) {
    layout.button("down", downPageButton);
    layout.button("next", nextPageButton);
  }

  @Override
  public void handleAction(ActionContext<PagedEmbed> ctx) {
    if (!ctx.isActiveSession())
      return;
    final ActionSession<PagedEmbed> session = ctx.getActiveSession();
    final Message message = session.getMessage();
    final PagedEmbed builder = session.getPayload();
    ctx.deferEdit();
    if (ctx.hasButtonClicked(nextPageButton))
      currentPage = ((currentPage + 1) >= builder.size()) ? 0 : (currentPage + 1);
    else if (ctx.hasButtonClicked(downPageButton))
      currentPage = ((currentPage - 1) < 0) ? (builder.size() - 1) : (currentPage - 1);
    else return;
    session.keepalive(10000);;
    message.editMessageEmbeds(builder.build(currentPage)).queue();
  }
}
