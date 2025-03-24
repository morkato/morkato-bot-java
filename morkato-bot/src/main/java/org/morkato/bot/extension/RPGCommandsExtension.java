package org.morkato.bot.extension;

import org.jetbrains.exposed.sql.Database;
import org.morkato.bot.commands.ArtCommand;
import org.morkato.bot.commands.RepositoryTest;
import org.morkato.bot.commands.TestCommand;
import org.morkato.bot.commands.TrainerCommand;
import org.morkato.bmt.context.ExtensionSetupContext;
import org.morkato.bmt.annotation.RegistryExtension;
import org.morkato.bmt.extensions.BaseExtension;
import org.morkato.bmt.extensions.Extension;

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
    ctx.setCurrentCommand(TestCommand.class);
    ctx.setCommandName("test");
  }
}
