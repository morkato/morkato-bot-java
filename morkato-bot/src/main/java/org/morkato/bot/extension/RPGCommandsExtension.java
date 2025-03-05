package org.morkato.bot.extension;

import org.morkato.bmt.context.ExtensionSetupContext;
import org.morkato.bmt.annotation.RegistryExtension;
import org.morkato.bmt.extensions.BaseExtension;
import org.morkato.bmt.extensions.Extension;

@RegistryExtension
public class RPGCommandsExtension extends BaseExtension implements Extension {
  @Override
  public void setup(ExtensionSetupContext ctx) {
    ctx.setCurrentCommand(org.morkato.bot.commands.ArtCommand.class);
    ctx.setCommandName("art");
    ctx.setCommandName("arte");
    ctx.setCurrentCommand(org.morkato.bot.commands.RepositoryTest.class);
    ctx.setCommandName("repository");
  }
}
