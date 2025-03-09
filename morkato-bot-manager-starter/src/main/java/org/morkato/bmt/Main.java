package org.morkato.bmt;

import org.morkato.bmt.registration.RegistrationFactory;
import org.morkato.bmt.reflection.ReflectionProvider;
import org.morkato.bmt.annotation.NotRequired;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.impl.LoaderContextImpl;
import org.morkato.bmt.context.LoaderContext;
import org.morkato.bmt.loader.Loader;
import org.morkato.utility.MorkatoConfigLoader;
import org.morkato.utility.ClassInjectorMap;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sun.misc.SignalHandler;
import sun.misc.Signal;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Objects;

import static org.reflections.scanners.Scanners.TypesAnnotated;
import static org.reflections.scanners.Scanners.SubTypes;

public class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  public static void runApplication(Class<?> mainClass) throws Throwable {
    Properties properties = MorkatoConfigLoader.loadDefault();
    String token = properties.getProperty("morkato.bot.token");
    Objects.requireNonNull(token, "morkato.bot.token is required to run bot!");
    LOGGER.info("Proccess: {}", ManagementFactory.getRuntimeMXBean().getName());
    LOGGER.info("Creating a context to run discord bot ({} -- {}).", Main.class.getPackageName(), mainClass.getPackageName());
    Reflections reflections = new Reflections(mainClass.getPackageName(), SubTypes, TypesAnnotated);
    final ClassInjectorMap injector = new ClassInjectorMap(AutoInject.class, NotRequired.class);
    final RegistrationFactory registration = RegistrationFactory.createDefault();
    final LoaderContext context = new LoaderContextImpl(properties);
    final JDA jda = JDABuilder.createDefault(token)
      .setAutoReconnect(true)
      .addEventListeners(registration.createListener())
      .enableIntents(GatewayIntent.MESSAGE_CONTENT)
      .enableIntents(GatewayIntent.GUILD_MEMBERS)
      .build()
      .awaitReady();
    final Loader loader = new LoaderBuilder(injector)
      .inject(jda)
      .inject(jda.getSelfUser())
      .setFactoryContext(context)
      .setFactory(registration)
      .registerAllExtensions(ReflectionProvider.extractAllExtensions(reflections))
      .registerAllComponents(ReflectionProvider.extractAllComponents(reflections))
      .build();
    final TerminationMain termination = new TerminationMain(registration, jda);
    Signal.handle(new Signal("TERM"), termination);
    Signal.handle(new Signal("INT"), termination);
    Signal.handle(new Signal("HUP"), termination);
    loader.flush(); /* All content is loaded: commands, components, exceptions and arguments */
    jda.awaitShutdown();
  }

  public static void main(String[] args) throws Throwable {
    runApplication(Main.class);
  }

  static class TerminationMain implements SignalHandler {
    private final RegistrationFactory factory;
    private final JDA jda;

    public TerminationMain(RegistrationFactory factory,JDA jda) {
      this.factory = factory;
      this.jda = jda;
    }

    @Override
    public void handle(Signal sig) {
      factory.shutdown();
      jda.shutdown();
    }
  }
}