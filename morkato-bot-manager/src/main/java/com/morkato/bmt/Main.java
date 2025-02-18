package com.morkato.bmt;

import com.morkato.bmt.commands.CommandManager;
import com.morkato.bmt.function.ExceptionFunction;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  public static <T> void runApplication(
    Class<T> mainClass
  ) throws Exception {
    String token = System.getenv("BOT_TOKEN");
    if (token == null) {
      System.out.println("BOT_TOKEN is required!");
      return;
    }
    logger.info("Creating a context to run discord bot ({} -- {}).", Main.class.getPackageName(), mainClass.getPackageName());
    Map<Class<?>, Object> injected = new HashMap<>();
    Map<Class<?>,ExceptionFunction<?>> exceptions = new HashMap<>();
    ExtensionManager manager = ExtensionManager.from(mainClass.getPackage(), injected, exceptions);
    CommandManager commands = new CommandManager(exceptions);
    BotBuilder builder = new BotBuilder(token);
    commands.setPrefix("!");
    builder.build(manager, commands, injected);
  }
  public static void main(String[] args) throws Throwable {
    runApplication(Main.class);
  }
}
