package org.morkato.bmt.bot.extension;

import org.morkato.bmt.bot.commands.ArtCommand;
import org.morkato.bmt.bot.commands.RepositoryTest;
import org.morkato.bmt.bot.commands.TrainerCommand;
import org.morkato.bmt.bmt.context.ExtensionSetupContext;
import org.morkato.bmt.annotation.RegistryExtension;
import org.morkato.bmt.bmt.extensions.BaseExtension;
import org.morkato.bmt.bmt.extensions.Extension;

@RegistryExtension
public class RPGCommandsExtension extends BaseExtension implements Extension {
  @Override
  public void setup(ExtensionSetupContext ctx) {
    ctx.setCurrentCommand(ArtCommand.class);
    ctx.setCommandName("art");
    ctx.setCommandName("arte");
    ctx.setCurrentCommand(RepositoryTest.class);
    ctx.setCommandName("repository");
    ctx.setCurrentCommand(TrainerCommand.class);
    ctx.setCommandName("train");
    ctx.setCommandName("treino");
    ctx.setCommandName("trainar");
    ctx.setCommandName("trainer");
  }
}
