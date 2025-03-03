package org.morkato.bmt;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.argument.ArgumentParser;
import org.morkato.bmt.commands.CommandExecutor;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.utility.ClassInjectorMap;
import org.morkato.bmt.components.Extension;
import org.morkato.bmt.management.ComponentManager;
import org.morkato.bmt.management.CommandManager;
import org.morkato.bmt.management.ExtensionManager;
import net.dv8tion.jda.api.JDA;
import org.morkato.utility.LambdaUtil;
import org.morkato.utility.MorkatoConfigLoader;
import org.morkato.utility.exception.ValueAlreadyInjected;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sun.misc.Signal;

import javax.annotation.Nullable;
import java.util.Properties;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  public static <T> void runApplication(
    Class<T> mainClass
  ) throws Exception {
    Properties properties = MorkatoConfigLoader.loadDefault();
    String token = properties.getProperty("morkato.bot.token");
    Runtime runtime = Runtime.getRuntime();
    if (token == null) {
      System.out.println("morkato.bot.token is required to run bot!");
      return;
    }
    logger.info("Creating a context to run discord bot ({} -- {}).", Main.class.getPackageName(), mainClass.getPackageName());
    ClassInjectorMap injector = new ClassInjectorMap(AutoInject.class, Nullable.class);
    ComponentManager<Extension> components = ComponentManager.from(mainClass.getPackage());
    ExtensionManager extensions = ExtensionManager.from(components);
    CommandManager commands = new CommandManager();
    CommandExecutor executor = new CommandExecutor(commands);
    JDA jda = JDABuilder.createDefault(token)
      .setAutoReconnect(true)
      .addEventListeners(new BotListener(executor))
      .enableIntents(GatewayIntent.MESSAGE_CONTENT)
      .enableIntents(GatewayIntent.GUILD_MEMBERS)
      .build()
      .awaitReady();
    executor.setPrefix("!");
    SelfUser user = jda.getSelfUser();
    try {
      injector.inject(user);
    } catch (ValueAlreadyInjected exc) {}
    ExtensionLoader extensionLoader = new ExtensionLoader(commands, injector, properties);
    ComponentLoader componentLoader = new ComponentLoader(commands, commands.getExceptionManager(), ArgumentParser.getManager(), injector);
    extensionLoader.load(extensions);
    componentLoader.loadAll(components.from(Extension.class));
    for (Extension extension : extensionLoader.getLoaded()) {
      componentLoader.loadAll(components.from(extension));
    }
    executor.setReady();
    runtime.addShutdownHook(new Thread(LambdaUtil.createAll(executor::shutdown, extensionLoader::shutdown, jda::shutdown)));
    jda.awaitShutdown();
  }
  public static void main(String[] args) throws Throwable {
    runApplication(Main.class);
  }
}
