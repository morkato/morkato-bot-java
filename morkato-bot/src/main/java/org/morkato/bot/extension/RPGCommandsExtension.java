package org.morkato.bot.extension;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.components.BaseExtension;
import org.morkato.bmt.components.Extension;
import org.morkato.bmt.context.ExtensionSetupContext;

@MorkatoComponent
public class RPGCommandsExtension extends BaseExtension implements Extension {
  @Override
  public void setup(ExtensionSetupContext ctx) {
    ctx.setCurrentCommand(org.morkato.bot.commands.ArtCommand.class);
    ctx.setCommandName("art");
    ctx.setCommandName("arte");
  }
}
