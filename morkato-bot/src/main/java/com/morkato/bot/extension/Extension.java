package com.morkato.bot.extension;

import com.morkato.bmt.ApplicationContextBuilder;
import com.morkato.bmt.commands.CommandManager;
import com.morkato.bmt.annotation.RegistryExtension;
import com.morkato.bmt.BaseExtension;
import com.morkato.bmt.commands.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RegistryExtension
public class Extension extends BaseExtension {
  private static Logger logger = LoggerFactory.getLogger(Extension.class);
  @Override
  public void start(ApplicationContextBuilder app) {
    logger.info("Started extension!");

  }
  @Override
  public void setup(CommandManager commands) {
    logger.info("Up extension!");
    commands.setCommandName(com.morkato.bot.commands.Test.class, "teste-nome");
    commands.setCommandName(com.morkato.bot.commands.Test.class, "teste-alias");
    commands.setCommandName(com.morkato.bot.commands.TestWithArgument.class, "teste-args");
  }

  public void onException(Context<?> ctx, Throwable exc) {
    System.out.println("Outro: " + exc);
  }
}
