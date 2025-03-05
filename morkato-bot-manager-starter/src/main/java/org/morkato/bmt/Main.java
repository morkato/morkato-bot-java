package org.morkato.bmt;

import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.annotation.RegistryExtension;
import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.loader.SimpleComponentLoaderFactory;
import org.morkato.bmt.loader.LoaderRegistrationFactory;
import org.morkato.bmt.commands.CommandExecutor;
import org.morkato.bmt.impl.LoaderContextImpl;
import org.morkato.bmt.context.LoaderContext;
import org.morkato.bmt.loader.Loader;
import org.morkato.bmt.management.*;
import org.morkato.bmt.annotation.NotRequired;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.utility.MorkatoConfigLoader;
import org.morkato.utility.ClassInjectorMap;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  public static <T> void runApplication(
    Class<T> mainClass
  ) throws Exception {
    String pid = ManagementFactory.getRuntimeMXBean().getName();
    System.out.println("Proccess: " + pid);
    Properties properties = MorkatoConfigLoader.loadDefault();
    String token = properties.getProperty("morkato.bot.token");
    if (token == null) {
      System.out.println("morkato.bot.token is required to run bot!");
      return;
    }
    logger.info("Creating a context to run discord bot ({} -- {}).", Main.class.getPackageName(), mainClass.getPackageName());
    Reflections reflections = new Reflections(mainClass.getPackageName(), SubTypes, TypesAnnotated);
    Set<Class<? extends Extension>> sweptExtensions = reflections.get(SubTypes.of(Extension.class).add(TypesAnnotated.with(RegistryExtension.class)).asClass())
      .stream()
      .map(clazz -> (Class<? extends Extension>)clazz.asSubclass(Extension.class))
      .collect(Collectors.toSet());
    Set<Class<?>> sweptComponents = reflections.getTypesAnnotatedWith(MorkatoComponent.class);
    final String prefix = properties.getProperty("morkato.bot.prefix", "!!");
    final ClassInjectorMap injector = new ClassInjectorMap(AutoInject.class, NotRequired.class);
    final ComponentManager components = new ComponentManager();
    final ExtensionManager extensions = new ExtensionManager();
    final ArgumentManager arguments = ArgumentManager.get();
    final CommandExceptionManager exceptions = new CommandExceptionManager();
    final CommandManager commands = new CommandManager(exceptions, arguments);
    final CommandExecutor executor = new CommandExecutor(commands, arguments);
    final LoaderContext loaderContext = new LoaderContextImpl(properties);
    final LoaderRegistrationFactory registration = new SimpleComponentLoaderFactory(commands, exceptions, arguments);
    final Loader loader = new Loader(loaderContext, registration, injector);
    final JDA jda = JDABuilder.createDefault(token)
      .setAutoReconnect(true)
      .addEventListeners(new BotListener(executor))
      .enableIntents(GatewayIntent.MESSAGE_CONTENT)
      .enableIntents(GatewayIntent.GUILD_MEMBERS)
      .build()
      .awaitReady();
    final TerminationMain termination = new TerminationMain(executor, jda);
    Signal.handle(new Signal("TERM"), termination);
    Signal.handle(new Signal("INT"), termination);
    Signal.handle(new Signal("HUP"), termination);
    RegisterManagement.registerAll(extensions, sweptExtensions);
    RegisterManagement.registerAll(components, sweptComponents);
    injector.injectIfAbsent(jda);
    injector.injectIfAbsent(jda.getSelfUser());
    loader.loadExtensions(extensions);
    loader.loadComponents(components);
    loader.flip();
    sweptExtensions.clear();
    sweptComponents.clear();
    executor.setPrefix(prefix);
    executor.setReady();
  }

  public static void main(String[] args) throws Throwable {
    runApplication(Main.class);
  }

  static class TerminationMain implements SignalHandler {
    private final CommandExecutor executor;
    private final JDA jda;

    public TerminationMain(CommandExecutor executor, JDA jda) {
      this.executor = executor;
      this.jda = jda;
    }

    @Override
    public void handle(Signal sig){
      executor.shutdown();
      jda.shutdown();
    }
  }
}
