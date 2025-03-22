package org.morkato.bmt;

import org.morkato.bmt.invoker.CommandInvoker;
import org.morkato.bmt.invoker.Invoker;
import org.morkato.bmt.listener.HelloListener;
import org.morkato.bmt.listener.TextCommandListener;
import org.morkato.bmt.registration.RegistrationFactory;
import org.morkato.bmt.reflection.ReflectionProvider;
import org.morkato.bmt.annotation.NotRequired;
import org.morkato.bmt.annotation.AutoInject;
import org.morkato.bmt.impl.LoaderContextImpl;
import org.morkato.bmt.context.LoaderContext;
import org.morkato.bmt.loader.Loader;
import org.morkato.bmt.registration.impl.MorkatoBotManagerRegistration;
import org.morkato.utility.MorkatoConfigLoader;
import org.morkato.utility.ClassInjectorMap;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
    LOGGER.info("Yet spawned process: {}", ManagementFactory.getRuntimeMXBean().getName());
    LOGGER.info("Creating a context to run discord bot ({} -- {}).", Main.class.getPackageName(), mainClass.getPackageName());
    final MorkatoBotManagerRegistration registration = RegistrationFactory.createDefault();
    final CommandInvoker invoker = Invoker.createDefaultCommandInvoker(registration);
    final JDA jda = JDABuilder.createDefault(token)
      .setAutoReconnect(true)
      .addEventListeners(new HelloListener())
      .addEventListeners(new TextCommandListener(registration, invoker) {
        @Override
        public String getPrefix(){
          return "!!";
        }
      })
      .enableIntents(GatewayIntent.MESSAGE_CONTENT)
      .enableIntents(GatewayIntent.GUILD_MEMBERS)
      .build()
      .awaitReady();
    Runtime.getRuntime().addShutdownHook(new Thread(new TerminationMain(registration, invoker, jda)));
    {
      final Reflections reflections = new Reflections(mainClass.getPackageName(), SubTypes, TypesAnnotated);
      final ClassInjectorMap injector = new ClassInjectorMap(AutoInject.class, NotRequired.class);
      final LoaderContext context = new LoaderContextImpl(properties);
      final Loader loader = new LoaderBuilder(injector)
        .inject(jda)
        .inject(jda.getSelfUser())
        .setFactoryContext(context)
        .setFactory(registration)
        .registerAllExtensions(ReflectionProvider.extractAllExtensions(reflections))
        .registerAllComponents(ReflectionProvider.extractAllComponents(reflections))
        .build();
      injector.write(registration);
      loader.flush(); /* All content is loaded: commands, components, exceptions and arguments */
    }
    invoker.start();
    /* Para prevenções... Rode GC, por favor, rode >_< */
    System.gc();
  }

  public static void main(String[] args) throws Throwable {
    runApplication(Main.class);
  }

  static class TerminationMain implements Runnable {
    private final CommandInvoker invoker;
    private final RegistrationFactory factory;
    private final JDA jda;

    public TerminationMain(RegistrationFactory factory, CommandInvoker listener, JDA jda) {
      this.invoker = listener;
      this.factory = factory;
      this.jda = jda;
    }

    @Override
    public void run() {
      factory.shutdown();
      jda.shutdown();
      invoker.shutdown();
    }
  }
}