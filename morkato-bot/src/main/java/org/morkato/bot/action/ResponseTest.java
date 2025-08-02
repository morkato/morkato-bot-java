package org.morkato.bot.action;

import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.morkato.mcbmt.actions.ActionSession;
import org.morkato.mcbmt.actions.ButtonAction;
import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.actions.ActionContext;
import org.morkato.mcbmt.actions.LayoutAction;

public class ResponseTest implements ActionHandler<String> {
  private ButtonAction test = ButtonAction.builder()
    .style(ButtonStyle.PRIMARY)
    .label("Botão")
    .build();
  private ButtonAction other = ButtonAction.builder()
    .style(ButtonStyle.PRIMARY)
    .label("Outro Botão")
    .build();

  @Override
  public void registerAction(LayoutAction layout) {
    layout.button("id-test", test);
    layout.button("id-test-outro", other);
  }

  @Override
  public void handleAction(ActionContext<String> ctx) {
    ctx.reply("Você clicou em um botão!").queue();
    ctx.getActiveSession().keepalive(10000);
  }

  @Override
  public void onSessionExpired(ActionSession<String> session) {
    session.cleanLayout();
    session.getMessage()
      .reply("Sua sessão hospedada no slot: " + session.getSlot() + " foi dropada com sucesso (i).")
      .queue();
  }
}
